package com.ihrm.domain.system;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 */
@Entity
@Table(name = "pe_permission_api")
public class PermissionApi implements Serializable {
    private static final long serialVersionUID = -1803315043290784820L;
    /**
     * 主键，同权限ID
     */
    @Id
    private String id;
    /**
     * 链接
     */
    private String apiUrl;
    /**
     * 请求类型
     */
    private String apiMethod;
    /**
     * 权限等级，1为通用接口权限，2为需校验接口权限
     */
    private String apiLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    public String getApiMethod() {
        return apiMethod;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    public String getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(String apiLevel) {
        this.apiLevel = apiLevel;
    }
}