package com.zhouyuan.shiro.demo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Before;
import org.junit.Test;

/**
 * @description: shiro框架授权测试类
 * @author: yuand
 * @create: 2019-12-18 14:50
 **/
public class ShiroTest02 {

    private Subject subject;
    @Before
    public void init(){
        //1.根据配置文件创建SecurityManagerFactory
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-test-02.ini");
        //2.通过工厂获取SecurityManager
        SecurityManager securityManager = factory.getInstance();
        //3.将SecurityManager绑定到当前运行环境
        SecurityUtils.setSecurityManager(securityManager);
        //4.从当前运行环境中构造subject
        subject = SecurityUtils.getSubject();

    }
    @Test
    public void AuthorizationTest(){
        String userName = "Zoro";
        String passwd = "123456";
        //5.构造shiro登录的凭据
        UsernamePasswordToken token = new UsernamePasswordToken(userName,passwd);
        //6.主体登陆
        subject.login(token);
        //7.验证用户是否登录成功
        System.out.println("用户是否登录成功:" + subject.isAuthenticated());
        //8.获取登录成功的数据
        System.out.println(subject.getPrincipal());

        //登录成功之后，完成授权
        //授权：检验当前登录用户是否具有操作权限，是否具有某个角色
        System.out.println(subject.hasRole("swordsman"));
        System.out.println(subject.hasRole("ninja"));
        System.out.println(subject.isPermitted("kill"));
        System.out.println(subject.isPermitted("run"));
    }
}
