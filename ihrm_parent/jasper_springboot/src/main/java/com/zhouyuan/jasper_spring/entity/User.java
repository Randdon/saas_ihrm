package com.zhouyuan.jasper_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description: jasper-使用JavaBean填充pdf样板数据的实体Bean
 * @author: yuand
 * @create: 2020-01-17 15:46
 **/
@Data
@AllArgsConstructor
public class User {

    private String id;
    private String username;
    private String company;
    private String department;
    private String mobile;
}
