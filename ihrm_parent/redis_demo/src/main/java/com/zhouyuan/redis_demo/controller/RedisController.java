package com.zhouyuan.redis_demo.controller;

import com.zhouyuan.redis_demo.service.GenerateNum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: redisController测试
 * @author: yuand
 * @create: 2020-04-25 11:17
 * @modifiedBy: yuand
 **/
@RequestMapping(value = "/test")
@RestController
public class RedisController {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    GenerateNum generateNum;

    @RequestMapping(value = "/redis",method = RequestMethod.GET)
    public Long redisTest(){
        return generateNum.redisIncr();
    }

    @RequestMapping(value = "/normal",method = RequestMethod.GET)
    public Integer normal(){
        return generateNum.getNum();
    }
}
