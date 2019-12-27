package com.ihrm.domain.system.response;

import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;

import java.io.Serializable;
import java.util.*;

/**
 * @description: 用户信息返回实体
 * @author: yuand
 * @create: 2019-12-17 10:25
 **/
public class ProfileResult implements Serializable {

    private String mobile;
    private String username;
    private String company;
    private String companyId;

    private Map<String,Object> roles = new HashMap<>(16);

    public ProfileResult(User user, List<Permission> permissions) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();

        Set<String> menus = new HashSet<>(16);
        Set<String> points = new HashSet<>(16);
        Set<String> apis = new HashSet<>(16);

        permissions.stream().forEach(permission -> {
            if (permission.getType().equals(3)){
                apis.add(permission.getCode());
            } else if (permission.getType().equals(2)){
                points.add(permission.getCode());
            } else {
                menus.add(permission.getCode());
            }
        });

        this.roles.put("apis",apis);
        this.roles.put("menus",menus);
        this.roles.put("points",points);
    }

    public ProfileResult(User user) {
        this.mobile = user.getMobile();
        this.username = user.getUsername();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();

        Set<String> menus = new HashSet<>(16);
        Set<String> points = new HashSet<>(16);
        Set<String> apis = new HashSet<>(16);
        Set<Role> roleSet = user.getRoles();

        roleSet.stream().forEach(role -> role.getPermissions().stream().forEach(permission -> {
            if (permission.getType().equals(3)){
                apis.add(permission.getCode());
            } else if (permission.getType().equals(2)){
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

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
