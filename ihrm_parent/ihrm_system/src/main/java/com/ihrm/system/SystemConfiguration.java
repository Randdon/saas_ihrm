package com.ihrm.system;

import com.zhouyuan.saas.ihrm.interceptor.JwtInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @description: 配置类
 * @author: yuand
 * @create: 2019-12-17 17:34
 **/
@Configuration
public class SystemConfiguration extends WebMvcConfigurationSupport {

    @Autowired
    private JwtInterceptor jwtInterceptor;

    /**
     * 添加拦截器的配置
     * @param registry
     */
    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")//2.指定拦截器的url地址
                .excludePathPatterns("/sys/login","/frame/register/**");//3.指定不拦截的url地址
    }
}
