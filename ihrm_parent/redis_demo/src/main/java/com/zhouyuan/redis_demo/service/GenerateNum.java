package com.zhouyuan.redis_demo.service;

import java.time.format.DateTimeFormatter;

/**
 * @description: 生成唯一数
 * @author: yuand
 * @create: 2020-04-25 09:26
 * @modifiedBy: yuand
 **/
public class GenerateNum {

    public static int count = 0;

    public int getNum(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return count++;

    }
}
