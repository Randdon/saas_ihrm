package com.ihrm.system.shiro.config;

import com.ihrm.system.shiro.realm.UserRealm;
import com.zhouyuan.saas.ihrm.shiro.realm.IhrmRealm;
import com.zhouyuan.saas.ihrm.shiro.session.IhrmShiroSessionManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: shiro配置类
 * @author: yuand
 * @create: 2019-12-26 14:49
 **/
@Configuration
public class ShiroConfig {

    /**
     *  创建自定义realm
     * @return
     */
    @Bean
    public IhrmRealm realm(){
        return new UserRealm();
    }

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;

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
     * redis缓存管理器
     * @return
     */
    public RedisCacheManager redisCacheManager(){
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * sessionDao-存取session信息
     * @return
     */
    public RedisSessionDAO sessionDAO(){
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * 自定义会话管理器
     * @return
     */
    public IhrmShiroSessionManager sessionManager(){
        IhrmShiroSessionManager sessionManager = new IhrmShiroSessionManager();
        sessionManager.setSessionDAO(sessionDAO());
        //禁用cookie 因为shiro默认会将session存储到cookie中
        sessionManager.setSessionIdCookieEnabled(false);
        //禁用url重写，默认会将url重写为url+jssessionid=id
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 创建安全管理器
     * @param realm
     * @return
     */
    @Bean
    public SecurityManager securityManager(IhrmRealm realm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(realm);
        //将自定义的redis缓存管理器注册到安全管理器中
        securityManager.setCacheManager(redisCacheManager());
        //将自定义的会话管理器注册到安全管理器中
        securityManager.setSessionManager(sessionManager());
        return securityManager;
    }

    /**
     * 配置shiro的过滤器工厂
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
        shiroFilterFactoryBean.setLoginUrl("/autherror?code=1");//未登录跳转页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/autherror?code=2");//未授权跳转页面
        /**
         * 4.设置过滤器集合
         * 设置所有的过滤器：有顺序map
         *     key = 拦截的url地址
         *     value = 过滤器类型
         */
        Map<String,String> filterMap = new LinkedHashMap<>(16);
        //anon表示当前请求地址可以匿名访问
        filterMap.put("/sys/login","anon");
        filterMap.put("/autherror","anon");
        //authc表示当前请求地址必须认证之后可以访问
        filterMap.put("/**","authc");
        //过滤器也可以完成url鉴权，但本例中使用另一种方式：shiro注解来完成
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
