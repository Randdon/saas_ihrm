package com.zhouyuan.redis_demo;

import com.zhouyuan.redis_demo.service.GenerateNum;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;

/**
 * @description: 多线程测试
 * @author: yuand
 * @create: 2020-04-24 17:29
 * @modifiedBy: yuand
 **/
public class MultiThread2 implements Runnable{
    private static CountDownLatch countDownLatch = new CountDownLatch(50);
    private GenerateNum generateNum = new GenerateNum();
    private static List<Integer> list = new Vector<>(50);

    @Override
    public void run() {
        countDownLatch.countDown();
        list.add(generateNum.getNum());
    }

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 50; i++) {
            new Thread(new MultiThread2()).start();
        }
        countDownLatch.await();
        Thread.sleep(1000);
        list.stream().sorted(Integer::compareTo).forEach(System.out::println);
    }
}
