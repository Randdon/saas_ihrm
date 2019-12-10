package com.zhouyuan.saas.ihrm.ihrm_system;

import com.ihrm.domain.system.User;
import com.ihrm.system.dao.RoleDao;
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
    @Autowired
    RoleDao roleDao;

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

    /**
     * jpa的多对多关系分为维护端和被维护端，删除维护端的记录会影响关联表中的记录
     * 但是删除被维护端的记录不会影响关联表中的记录
     */
    @Test
    public void jpaMany2ManyTest(){
        /**
         * 删除用户的同时也会删除用户_角色关联表
         */
        //userDao.deleteById("1066370498633486336");
        /**
         * 删除角色的时候不会删除用户_角色关联表
         * 但会删除角色_权限关联表
         */
        roleDao.deleteById("1062944989845262336");
    }
}
