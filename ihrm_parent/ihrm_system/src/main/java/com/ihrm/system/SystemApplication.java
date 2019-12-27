package com.ihrm.system;

import com.zhouyuan.saas.ihrm.utils.IdWorker;
import com.zhouyuan.saas.ihrm.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

//Springboot的包扫描
@SpringBootApplication(scanBasePackages = "com")
//jpa注解的扫描
@EntityScan(value = "com.ihrm.domain.system")
@EnableEurekaClient//开启eureka客户端配置
public class SystemApplication {
    public static void main(String[] args) {
        SpringApplication.run(SystemApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }

    //解决加了拦截器后，jpa多对多映射懒加载no session的问题
    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter() {
        return new OpenEntityManagerInViewFilter();
    }
}
