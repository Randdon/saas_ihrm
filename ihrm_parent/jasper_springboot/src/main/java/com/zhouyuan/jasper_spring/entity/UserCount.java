package com.zhouyuan.jasper_spring.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @description: 用于显示饼状图的企业员工数量的实体类
 * @author: yuand
 * @create: 2020-01-19 16:24
 **/
@Data
@AllArgsConstructor
public class UserCount {
    private String company;
    private Long count;
}
