package com.zhouyuan.shiro.demo;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 自定义shiro的realm
 * 自定义realms对象
 *      继承AuthorizingRealm
 *          重写方法
 *              doGetAuthorizationInfo：授权
 *                  获取到用户的授权数据（用户的权限数据）
 *              doGetAuthenticationInfo：认证
 *                  根据用户名密码登录，将用户数据保存（安全数据）
 * !ATTENTION!: 此demo所有涉及数据库查询的地方只用假数据做测试
 * @author: yuand
 * @create: 2019-12-18 16:19
 **/
public class PermissionRealm extends AuthorizingRealm {

    /**
     * 自定义realm名称
     */
    @Override
    public void setName(String name) {
        super.setName("permissionRealm");
    }

    /**
     * 授权:授权的主要目的就是根据认证数据获取到用户的权限信息
     * @param principalCollection 包含了所有已认证的安全数据
     * @return AuthorizationInfoInfo：授权数据
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取安全数据  此处是username，也可以是用户id，此demo只有一个用户进行登录，所以获取第一个认证数据即可
        String userName = principalCollection.getPrimaryPrincipal().toString();
        //2.根据id或者名称查询用户-----------------------------此处省略
        //3.查询用户的角色和权限信息-----------------------------此处省略
        List<String> roles = Arrays.asList("swordsman", "dumb-ass");
        List<String> perms = Arrays.asList("kill", "sword", "dumb");
        //4.构造返回
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //设置角色集合
        info.addRoles(roles);
        //设置权限集合
        info.addStringPermissions(perms);
        return info;
    }

    /**
     *    认证：认证的主要目的，比较用户名和密码是否与数据库中的一致
     *     将安全数据存入到shiro进行保管
     * @param authenticationToken 登录构造的UsernamePasswordToken(也可以是其他token，此demo中用UsernamePasswordToken)
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.构造UsernamePasswordToken
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        //2.获取输入的用户名密码
        String username = upToken.getUsername();
        String passwd = new String(upToken.getPassword());
        //3.根据用户名查询数据库-----------------------------此处省略
        //4.比较密码和数据库中的密码是否一致（密码可能需要加密）
        if ("123456".equals(passwd)){
            //5.如果成功，向shiro存入安全数据
            //构造器参数列表：1.安全数据，（此处传的是用户名，可以传用户id或者其他有必要存储的数据）2.密码。3。当前realm域名称
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(username,passwd,getName());
            return info;
        } else {
            //6.失败，抛出异常或返回null
            throw new RuntimeException("用户名或密码错误");
        }
    }
}
