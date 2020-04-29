package com.zhouyuan.redis_demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

/**
 * @description: 生成唯一数
 * @author: yuand
 * @create: 2020-04-25 09:26
 * @modifiedBy: yuand
 **/
@Service
public class GenerateNum {

    public static int count = 0;

    public int getNum(){
        //在接口并发测试中不加该语句也会出现并发错误
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return count++;
    }

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    public long redisIncr(){
        return stringRedisTemplate.opsForValue().increment("redis");
    }
}
