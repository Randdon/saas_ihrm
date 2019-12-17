package com.ihrm.domain.system.response;

import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.zhouyuan.saas.ihrm.utils.PermissionConstants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @description: 用户信息返回实体
 * @author: yuand
 * @create: 2019-12-17 10:25
 **/
public class ProfileResult {

    private String mobile;
    private String username;
    private String company;
    private Map<String,Object> roles = new HashMap<>(16);


    public ProfileResult(User user) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.company = user.getCompanyName();

        Set<String> menus = new HashSet<>(16);
        Set<String> points = new HashSet<>(16);
        Set<String> apis = new HashSet<>(16);
        Set<Role> roleSet = user.getRoles();

        roleSet.stream().forEach(role -> role.getPermissions().stream().forEach(permission -> {
            if (permission.getType().equals(PermissionConstants.PY_API)){
                apis.add(permission.getCode());
            } else if (permission.getType().equals(PermissionConstants.PY_POINT)){
                points.add(permission.getCode());
            } else {
                menus.add(permission.getCode());
            }
        }));

        this.roles.put("apis",apis);
        this.roles.put("menus",menus);
        this.roles.put("points",points);
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Map<String, Object> getRoles() {
        return roles;
    }

    public void setRoles(Map<String, Object> roles) {
        this.roles = roles;
    }
}
