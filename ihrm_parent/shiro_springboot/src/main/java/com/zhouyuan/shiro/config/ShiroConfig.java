package com.zhouyuan.shiro.config;

import com.zhouyuan.shiro.realm.CustomRealm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 */
@Configuration
public class ShiroConfig {

    /**
     *  创建realm
     * @return
     */
    @Bean
    public CustomRealm getCustomRealm(){
        return new CustomRealm();
    }

    /**
     * 创建安全管理器
     * @param realm
     * @return
     */
    @Bean
    public SecurityManager getSecurityManager(CustomRealm realm){
        //DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager(realm);
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        return securityManager;
    }

    /**
     * 配置shiro的过滤器工厂
     * 在web程序中，shiro进行权限控制全部是通过一组过滤器集合进行控制
     * @param securityManager
     * @return
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        //1.创建过滤器工厂
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //2.设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //3.通用配置（跳转登录页面，未授权跳转的页面）
        shiroFilterFactoryBean.setLoginUrl("/autherror?code=1");//登录页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/autherror?code=2");//未授权跳转页面
        /**
         * 4.设置过滤器集合
         * 设置所有的过滤器：有顺序map
         *     key = 拦截的url地址
         *     value = 过滤器类型
         */
        Map<String,String> filterMap = new LinkedHashMap<>(16);
        //当前请求地址可以匿名访问
        filterMap.put("/user/home","anon");
        //当前请求地址必须认证之后可以访问
        filterMap.put("/user/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 开启对shior注解的支持
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}
