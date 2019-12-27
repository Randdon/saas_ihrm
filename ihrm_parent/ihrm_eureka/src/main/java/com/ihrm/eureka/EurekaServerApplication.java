package com.ihrm.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @description: eureka服务端的启动类
 * @author: yuand
 * @create: 2019-12-27 15:23
 **/
@SpringBootApplication
@EnableEurekaServer//开启eureka服务端配置
public class EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class,args);
    }
}
