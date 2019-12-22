package com.zhouyuan.shiro.realm;

import com.zhouyuan.shiro.domain.User;
import com.zhouyuan.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

public class CustomRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;
    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 认证方法
     * @param authenticationToken 在本案例中是UsernamePasswordToken，可以传递用户名密码
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取登录的用户名密码（token）
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String username = upToken.getUsername();
        String passwd = upToken.getPassword().toString();
        //2.根据用户名查询数据库
        User user = userService.findByName(username);
        //3.判断用户是否存在或者密码是否一致
        if (null != user && passwd.equals(user.getPassword())){
            //4.如果一致，向shiro存入安全数据
            //SimpleAuthenticationInfo构造器参数列表：1.安全数据，（此处存的是user对象）2.密码。3。当前realm域名称
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, passwd, getName());
            return info;
        }
        //5.不一致，返回null（抛出异常）
        return null;
    }
}
