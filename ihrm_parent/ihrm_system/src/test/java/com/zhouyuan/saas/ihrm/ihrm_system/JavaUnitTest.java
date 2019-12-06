package com.zhouyuan.saas.ihrm.ihrm_system;

import com.ihrm.domain.system.User;
import com.ihrm.system.dao.UserDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @description: Integer的值在-128到127时，Integer对象是在IntegerCache.cache产生，会复用已有对象，也就是说，
 * 这个区间的Integer可以直接用等号进行判断。
 * Integer的值在-128到127之外时，Integer对象在堆上产生，不会复用已有对象，用等号会返回false。
 * 建议老老实实用equals()方法来比较Integer对象。
 * @author: yuand
 * @create: 2019-11-29 11:24
 **/
public class JavaUnitTest extends IhrmSystemApplicationTests{

    @Autowired
    UserDao userDao;

    @Test
    public void test(){
        integerTest(129);
        System.out.println("**************************************");
        integerTest(127);
    }

    /**
     * Integer的值在-128到127时，Integer对象是在IntegerCache.cache产生，会复用已有对象，也就是说，这个区间的Integer可以直接用等号进行判断。
     * Integer的值在-128到127之外时，Integer对象在堆上产生，不会复用已有对象，用等号会返回false。
     * 建议老老实实用equals()方法来比较Integer对象。
     * @param test
     */
    public void integerTest(int test){
        Integer integer = test;
        Integer integer1 = test;
        System.out.println(integer == test);
        System.out.println(integer == integer1);
        System.out.println(integer.intValue() == integer1.intValue());
        System.out.println(integer.equals(integer1));
    }

    @Test
    public void jpaTest(){
        List<String> ids = new ArrayList<>(3);
        ids.add("1063705482939731968");
        Optional<User> byId = userDao.findById("1063705482939731968");
        System.out.println(byId.get());
        //List<String> ids = Arrays.asList("1063705482939731968", "1075383135459430400", "1075383135371350016");
        List<User> list = userDao.findAllById(ids);
        System.out.println(list.size());
    }
}
