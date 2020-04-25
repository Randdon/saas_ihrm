package com.zhouyuan.redis_demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.format.DateTimeFormatter;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
class RedisDemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    //对照组
    private static int normal = 0;
    private CountDownLatch countDownLatch = new CountDownLatch(1000);
    @Test
    public void testSetAndGet(){
        stringRedisTemplate.opsForValue().set("yd","test");
        String yd = stringRedisTemplate.opsForValue().get("yd");
        System.out.println(yd);
    }

    @Test
    public void testIncr(){
        stringRedisTemplate.opsForValue().increment("yd_incr");
    }

    @Test
    public void multiThreadTest() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            new TestThread().start();
        }
        countDownLatch.await();
        Thread.sleep(2000);
        System.out.println(normal);
        String redis = stringRedisTemplate.opsForValue().get("multiThreadTest");
        System.out.println(redis);
    }

    class TestThread extends Thread{
        @Override
        public void run() {
            countDownLatch.countDown();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            normal++;
            stringRedisTemplate.opsForValue().increment("multiThreadTest");
        }
    }
}
