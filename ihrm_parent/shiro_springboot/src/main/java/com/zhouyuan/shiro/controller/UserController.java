package com.zhouyuan.shiro.controller;

import com.zhouyuan.shiro.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@RestController
public class UserController {

    @Autowired
    private UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * 使用shiro注解鉴权：
     * @RequiresPermissions()  -- 访问此方法必须具备的权限
     * @RequiresRoles() -- 访问此方法必须具备的角色
     * 1.过滤器鉴权：如果权限信息不匹配setUnauthorizedUrl地址
     * 2.注解鉴权：如果权限信息不匹配，抛出AuthorizationException异常
     * @return
     */
    @RequiresPermissions("user-home")
    @RequestMapping(value = "/user/home")
    public String home() {
        return "访问个人主页成功";
    }

    //添加
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public String add() {
        return "添加用户成功";
    }
	
    //查询
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public String find() {
        return "查询用户成功";
    }
	
    //更新
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public String update(String id) {
        return "更新用户成功";
    }
	
    //删除
    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
    public String delete() {
        return "删除用户成功";
    }

    /**
     * 用户登录
     *  1.传统登录
     *      前端发送登录请求 => 接口部分获取用户名密码 => 程序员在接口部分手动控制
     *  2.shiro登录
     *      前端发送登录请求 => 接口部分获取用户名密码 => 通过subject.login =>  realm域的认证方法
     * @param username
     * @param password
     * @return
     */
	@RequestMapping(value="/login")
    public String login(String username,String password) {
        try {
            /**
             * 密码加密：
             *     shiro提供的md5加密
             *     Md5Hash:
             *      参数一：加密的内容
             *              111111   --- abcd
             *      参数二：盐（加密的混淆字符串）（此处把用户登录的用户名当做盐值）
             *              111111+混淆字符串
             *      参数三：加密次数
             *
             */
            password = new Md5Hash(password,username,3).toString();
            //构造登录令牌
            UsernamePasswordToken upToken = new UsernamePasswordToken(username, password);
            //获取subject
            Subject subject = SecurityUtils.getSubject();
            //调用subject进行登录，调用此方法后进入自定义realm中的认证方法进行认证
            subject.login(upToken);
            String sessionId = subject.getSession().getId().toString();
            return "登录成功，sessionId：" + sessionId;
        } catch (Exception e) {
            LOGGER.error("登陆异常：{}",e);
            return "用户名或密码错误";
        }
    }

    //删除
    @RequestMapping(value = "/autherror")
    public String auth(int code) {
        return code == 1 ? "未登录" : "未授权";
    }

    /**
     * 通过shiro的Subject.login()方法登录成功后，用户的认证信息实际上是保存在HttpSession中的
     * @param session
     * @return
     */
    @RequestMapping(value = "/show")
    public String show(HttpSession session) {
	    //获取session中的所有键值
        Enumeration<String> attributeNames = session.getAttributeNames();
        String element;
        //遍历session中的所有键值
        while (attributeNames.hasMoreElements()){
            //获取键值
            element = attributeNames.nextElement();
            /**
             * 根据键值获取session中的value值，可以从打印信息中看到在com.zhouyuan.shiro.realm.CustomRealm.doGetAuthenticationInfo
             * 认证方法中认证通过后存储的user对象安全数据
             */
            System.out.println("===name: "+ element +",value: " + session.getAttribute(element));
        }
        return "查看session成功";
    }


}
