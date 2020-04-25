package com.zhouyuan.redis_demo;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

/**
 * @description: 多线程测试
 * @author: yuand
 * @create: 2020-04-24 17:29
 * @modifiedBy: yuand
 **/
public class MultiThread {
    private static int i = 0;
    private static CountDownLatch countDownLatch = new CountDownLatch(50);
    class OneThread extends Thread{

        @Override
        public void run() {
            //必须有这句才会出现多线程下i的结果出错，并且这句必须在i++上面
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            i++;
            countDownLatch.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        MultiThread thread = new MultiThread();
        for (int i = 0; i < 50; i++) {
            thread.new OneThread().start();
        }
        countDownLatch.await();
        Thread.sleep(1000);
        System.out.println(i);
    }
}
