package com.ihrm.employee;

import com.zhouyuan.saas.ihrm.utils.IdWorker;
import com.zhouyuan.saas.ihrm.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@EnableEurekaClient//开启eureka客户端配置
@SpringBootApplication(scanBasePackages = "com")
@EntityScan("com.ihrm.domain.employee")
public class EmployeeApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeApplication.class, args);
    }

    @Bean
    public IdWorker idWorkker() {
        return new IdWorker(1, 1);
    }

    @Bean
    public JwtUtils jwtUtils(){
        return new JwtUtils();
    }
}