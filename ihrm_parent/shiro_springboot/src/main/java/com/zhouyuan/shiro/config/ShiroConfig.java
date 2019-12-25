package com.zhouyuan.shiro.config;

import com.zhouyuan.shiro.realm.CustomRealm;
import com.zhouyuan.shiro.session.CustomSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * shiro配置类
 * shiro整合redis的统一会话管理：SecurityManager -> sessionManager -> sessionDAO -> redisManager
 */
@Configuration
public class ShiroConfig {

    /**
     *  创建自定义realm
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
        //将自定义的会话管理器注册到安全管理器中
        securityManager.setSessionManager(sessionManager());
        //将自定义的redis缓存管理器注册到安全管理器中
        securityManager.setCacheManager(redisCacheManager());
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
        //filterMap.put("/user/home","anon");

        /**
         *  Q: 本例中使用过滤器配置url鉴权有个疑问：为什么必须是【user-home】或【系统管理员】字段，其它字段就不行
         *  A: CustomRealm.doGetAuthorizationInfo鉴权方法中的role和permission的set集合的来源决定了用的是哪个字段
         */

        //使用过滤器的形式配置请求地址的依赖权限，具有某种权限才能访问，不具备指定的权限，跳转到setUnauthorizedUrl地址
        //filterMap.put("/user/home","perms[user-home]");
        //使用过滤器的形式配置请求地址的依赖权限，具有某种角色才能访问，不具备指定的角色，跳转到setUnauthorizedUrl地址
        filterMap.put("/user/home","roles[系统管理员]");
        //当前请求地址必须认证之后可以访问
        filterMap.put("/user/**","authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 自定义会话管理器
     * @return
     */
    public DefaultWebSessionManager sessionManager(){
        CustomSessionManager customSessionManager = new CustomSessionManager();
        customSessionManager.setSessionDAO(sessionDAO());
        return customSessionManager;
    }

    /**
     * sessionDao-存取session信息
     * @return
     */
    public SessionDAO sessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * redis缓存管理器
     * @return
     */
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;

    /**
     * redis的控制器，操作redis
     * @return
     */
    public RedisManager redisManager(){
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host + ":" + port);
        return redisManager;
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
