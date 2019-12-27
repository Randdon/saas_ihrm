package com.ihrm.company;

import com.zhouyuan.saas.ihrm.utils.IdWorker;
import com.zhouyuan.saas.ihrm.utils.JwtUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

//Springboot的包扫描
@SpringBootApplication(scanBasePackages = "com")
//jpa注解的扫描
@EntityScan(value = "com.ihrm.domain.company")
@EnableEurekaClient//开启eureka客户端配置
public class CompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }

    @Bean
    public IdWorker idWorker(){
        return new IdWorker();
    }

    @Bean
    public JwtUtils jwtUtils() {
        return new JwtUtils();
    }
}
