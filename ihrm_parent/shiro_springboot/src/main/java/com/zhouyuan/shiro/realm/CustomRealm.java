package com.zhouyuan.shiro.realm;

import com.zhouyuan.shiro.domain.User;
import com.zhouyuan.shiro.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义realm域
 */
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    UserService userService;
    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    /**
     * 授权方法
     *      操作的时候，判断用户是否具有响应的权限
     *          先认证 -- 获取安全数据
     *          再授权 -- 根据安全数据获取用户具有的所有操作权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取已认证的用户数据
        User user = (User) principalCollection.getPrimaryPrincipal();
        Set<String> roles = new HashSet<>(16);//所有角色
        Set<String> perms = new HashSet<>(16);//所有权限
        user.getRoles().forEach(role -> {
            roles.add(role.getName());
            role.getPermissions().forEach(permission -> perms.add(permission.getCode()));
        });
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setRoles(roles);
        info.setStringPermissions(perms);
        return info;
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
        String passwd =  String.valueOf(upToken.getPassword());
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
