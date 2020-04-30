package com.zhouyuan.redis_demo.config.mysql;

import java.lang.annotation.*;

/**
 * @description: 自定义注解
 * @author: yuand
 * @create: 2020-04-29 17:34:04
 * @modifiedBy yuand
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TargetDateSource {
    String dataSource() default "";// 数据源
}
