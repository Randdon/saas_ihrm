package com.ihrm.domain.system.response;

import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 用户返回结果实体类
 * @author: yuand
 * @create: 2019-12-16 10:34
 **/
@Data
@NoArgsConstructor
public class UserResult implements Serializable {

    private static final long serialVersionUID = 4297464181093070302L;

    /**
     * ID
     */
    private String id;
    /**
     * 手机号码
     */
    private String mobile;
    /**
     * 用户名称
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    /**
     * 启用状态 0为禁用 1为启用
     */
    private Integer enableState;
    /**
     * 创建时间
     */
    private Date createTime;

    private String companyId;

    private String companyName;

    /**
     * 部门ID
     */
    private String departmentId;

    /**
     * 入职时间
     */
    private Date timeOfEntry;

    /**
     * 聘用形式
     */
    private Integer formOfEmployment;

    /**
     * 工号
     */
    private String workNumber;

    /**
     * 管理形式
     */
    private String formOfManagement;

    /**
     * 工作城市
     */
    private String workingCity;

    /**
     * 转正时间
     */
    private Date correctionTime;

    /**
     * 在职状态 1.在职  2.离职
     */
    private Integer inServiceStatus;

    private String departmentName;

    /**
     * 该员工所具有的角色id集合
     */
    private List<String> roleIds;

    private String staffPhoto;//用户头像

    public UserResult(User user) {
        BeanUtils.copyProperties(user,this);
        this.roleIds = user.getRoles().stream().map(Role::getId).collect(Collectors.toList());
    }
}
