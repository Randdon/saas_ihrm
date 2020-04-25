package com.zhouyuan.redis_demo;

import java.util.ArrayList;
import java.util.List;

public class UnSafeThreadDemo {

    public UnSafeThreadDemo() {

    }
    /**
     * @author xuyan
     */
    public static void main(String[] args) throws InterruptedException {
        final UserStat us = new UnSafeThreadDemo().new UserStat();
        List<Integer> list = new ArrayList<>(50);
        for (int i = 0; i < 500; i++) {
            new Thread("th1") {
                @Override
                public void run() {
                    list.add(us.getUserCount());
                }
            }.start();
        }
        list.sort(Integer::compareTo);
        list.forEach(System.out::println);
    }

    class UserStat {
        int userCount;
        public int getUserCount() {
            return userCount++;
        }
    }
}
