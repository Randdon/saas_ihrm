package com.zhouyuan.saas.ihrm.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * 微服务调用组件Feign的拦截器
 * 主要解决本项目中微服务A调用微服务B时需要认证的问题，即：Browser——>A—Feign—>B
 * 浏览器调用A服务时带有认证所需要的请求头信息，
 * 但是A服务通过Feign调用B服务时并不会默认将该请求头信息带给B服务
 * 所以需要在Feign调用B服务前将浏览器发送给A服务的请求拦截并获取到请求头信息
 * 再将该请求头信息塞到Feign调用B服务的请求中去
 * 这样就可以解决虽然浏览器带有认证信息，但请求A服务（A服务有通过Feign调用B服务）会报未登录的问题
 */
@Configuration
public class FeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor(){
        return new RequestInterceptor() {
            /**
             * 获取浏览器发送过来的请求属性并赋值给Feign的请求
             * @param requestTemplate 该参数即是Feign调用微服务时所用到的请求对象
             */
            @Override
            public void apply(RequestTemplate requestTemplate) {
                /**
                 * 通过RequestContextHolder获取浏览器发送的请求所携带的请求属性
                 */
                ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
                if (null != requestAttributes){
                    //获取浏览器发送的请求对象
                    HttpServletRequest request = requestAttributes.getRequest();
                    //获取浏览器发送的该请求的所有请求头名称
                    Enumeration<String> headerNames = request.getHeaderNames();
                    if (null != headerNames){
                        while (headerNames.hasMoreElements()){
                            //请求头名称
                            String headerName = headerNames.nextElement();
                            //请求头数据
                            String value = request.getHeader(headerName);
                            //赋值到Feign所要发起的调用其他微服务的请求对象中
                            requestTemplate.header(headerName,value);
                        }
                    }
                }
            }
        };
    }
}
