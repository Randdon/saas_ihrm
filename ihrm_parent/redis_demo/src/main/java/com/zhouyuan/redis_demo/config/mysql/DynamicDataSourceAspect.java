package com.zhouyuan.redis_demo.config.mysql;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @description: 定义切面，实现数据源切换
 * @author: yuand
 * @create: 2020-04-29 17:34
 * @modifiedBy: yuand
 **/
@Aspect
@Component
public class DynamicDataSourceAspect {
    @Around("execution(public * com.zhouyuan.redis_demo.service..*.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method targetMethod = methodSignature.getMethod();
        if (targetMethod.isAnnotationPresent(TargetDateSource.class)) {
            String targetDataSource = targetMethod.getAnnotation(TargetDateSource.class).dataSource();
            System.out.println("----------数据源是:" + targetDataSource + "------");
            DynamicDataSourceHolder.setDataSource(targetDataSource);
        }
        // 执行方法
        Object result = pjp.proceed();
        DynamicDataSourceHolder.clearDataSource();
        return result;
    }
}
