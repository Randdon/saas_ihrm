package com.ihrm.system.shiro.realm;

import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.User;
import com.ihrm.domain.system.response.ProfileResult;
import com.ihrm.system.service.PermissionService;
import com.ihrm.system.service.UserService;
import com.zhouyuan.saas.ihrm.shiro.realm.IhrmRealm;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: shiro自定义realm-用户登录认证realm
 * @author: yuand
 * @create: 2019-12-26 11:24
 **/
public class UserRealm extends IhrmRealm {
    @Autowired
    UserService userService;
    @Autowired
    PermissionService permissionService;

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //1.构造UsernamePasswordToken
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String mobile = upToken.getUsername();
        String passwd = String.valueOf(upToken.getPassword());
        //2.根据用户名查询数据库
        User user = userService.findByMobile(mobile);
        //3.判断用户是否存在或者密码是否一致
        if (null != user && user.getPassword().equals(passwd)){
            //4.如果一致，构造ProfileResult作为安全数据，并向shiro存入安全数据
            ProfileResult result;
            if ("user".equals(user.getLevel())){
                result = new ProfileResult(user);
            }else {
                Map<String,Object> map = new HashMap<>(1);
                if ("coAdmin".equals(user.getLevel())){
                    map.put("enVisible",1);
                }
                List<Permission> permissions = permissionService.findAll(map);
                result = new ProfileResult(user,permissions);
            }
            //SimpleAuthenticationInfo构造器参数列表：1.安全数据，（此处存的是user对象）2.密码。3。当前realm域名称
            return new SimpleAuthenticationInfo(result,passwd,getName());
        }
        //5.不一致，返回null，会抛出异常，标识用户名和密码不匹配
        return null;
    }
}
