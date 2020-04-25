package com.zhouyuan.shiro.demo;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Before;
import org.junit.Test;

/**
 * @description: shiro框架自定义realm域认证、授权测试类
 * @author: yuand
 * @create: 2019-12-18 14:50
 **/
public class ShiroTest03 {

    private Subject subject;
    @Before
    public void init(){
        //1.根据配置文件创建SecurityManagerFactory
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro-test-03.ini");
        //2.通过工厂获取SecurityManager
        SecurityManager securityManager = factory.getInstance();
        //3.将SecurityManager绑定到当前运行环境
        SecurityUtils.setSecurityManager(securityManager);
        //4.从当前运行环境中构造subject
        subject = SecurityUtils.getSubject();

    }

    /**
     * 根据shiro-test-03.ini中的配置，securityManager中已注册了我们的自定义realm：PermissionRealm，
     * 其中已有认证和授权方法
     */
    @Test
    public void AuthorizationTest(){
        String userName = "Zoro";
        String passwd = "123456";
        //5.构造shiro登录的凭据
        UsernamePasswordToken token = new UsernamePasswordToken(userName,passwd);
        //6.主体登陆 --》realm中的认证方法
        subject.login(token);
        //7.验证用户是否登录成功
        System.out.println("用户是否登录成功:" + subject.isAuthenticated());
        //8.获取登录成功的数据
        System.out.println(subject.getPrincipal());

        //登录成功之后，完成授权
        //授权：检验当前登录用户是否具有操作权限，是否具有某个角色 --》realm中的授权方法
        System.out.println("该用户是否具有swordsman角色：" + subject.hasRole("swordsman"));
        System.out.println("该用户是否具有ninja角色：" + subject.hasRole("ninja"));
        System.out.println("该用户是否具有kill权限：" + subject.isPermitted("kill"));
        System.out.println("该用户是否具有run权限：" + subject.isPermitted("run"));
    }

    @Test
    public void hashmd5(){
        String md5Hash = new Md5Hash("123456","lisi",3).toString();
        System.out.println(md5Hash);
    }
}
