package com.zhouyuan.saas.ihrm.shiro.realm;

import com.ihrm.domain.system.response.ProfileResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;

/**
 * @description: shiro自定义realm-公共realm
 * 公共的realm：获取安全数据，构造权限信息
 * @author: yuand
 * @create: 2019-12-26 11:17
 **/
public class IhrmRealm extends AuthorizingRealm {
    @Override
    public void setName(String name) {
        super.setName("ihrmRealm");
    }

    /**
     * 授权方法
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //1.获取安全数据
        ProfileResult userProfile = (ProfileResult)principalCollection.getPrimaryPrincipal();
        //2.获取权限信息
        Set<String> apis = (Set<String>) userProfile.getRoles().get("apis");
        //3.构造权限数据，返回值
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(apis);
        return info;
    }

    /**
     * 认证方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
