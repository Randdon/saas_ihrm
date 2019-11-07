package com.ihrm.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

//Springboot的包扫描
@SpringBootApplication(scanBasePackages = "com.ihrm.company")
//jpa注解的扫描
@EntityScan(value = "com.ihrm.domain.company")
public class CompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CompanyApplication.class,args);
    }

}
