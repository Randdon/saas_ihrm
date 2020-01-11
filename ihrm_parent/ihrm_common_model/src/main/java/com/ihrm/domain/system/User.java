package com.ihrm.domain.system;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ihrm.domain.poi.ExcelAttribute;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户实体类
 */
@Entity
@NoArgsConstructor
@Table(name = "bs_user")
@Data
public class User implements Serializable {
    private static final long serialVersionUID = 4297464181093070302L;
    /**
     * ID
     */
    @Id
    private String id;
    /**
     * 手机号码
     */
    @ExcelAttribute(sort = 2)
    private String mobile;
    /**
     * 用户名称
     */
    @ExcelAttribute(sort = 1)
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
    @ExcelAttribute(sort = 5)
    private Date timeOfEntry;

    /**
     * 聘用形式
     */
    @ExcelAttribute(sort = 4)
    private Integer formOfEmployment;

    /**
     * 工号
     */
    @ExcelAttribute(sort = 3)
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

    @ExcelAttribute(sort = 6)
    private String departmentName;

    /**
     * level
     *     String
     *          saasAdmin：saas管理员具备所有权限
     *          coAdmin：企业管理（创建租户企业的时候添加）
     *          user：普通用户（需要分配角色）
     */
    private String level;

    private String staffPhoto;//用户头像

    /**
     *  JsonIgnore
     *     : 忽略json转化
     *     此注解作用是json序列化时将java bean中的一些属性忽略掉，序列化和反序列化都受影响。
     *     Spring默认内置的即是jackson
     */
    @JsonIgnore
    @ManyToMany
    @JoinTable(name="pe_user_role",joinColumns={@JoinColumn(name="user_id",referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="role_id",referencedColumnName="id")}
    )
    private Set<Role> roles = new HashSet<Role>();//用户与角色   多对多

    public User(Object[] objects) {
        //因为excel模板的第一列为空，所以单元格数据值数组从索引1位置开始取值
        this.username = objects[1].toString();
        this.mobile = objects[2].toString();
        //因为excel上的数字采用科学计数法或poi解析成double类型的缘故，所以这里需要处理字符串格式
        this.workNumber = new DecimalFormat("#").format(objects[3]);
        //poi会将excel上的数字都解析成double型，所以此处需要转型
        this.formOfEmployment = ((Double)objects[4]).intValue();
        this.timeOfEntry = (Date)objects[5];
        //TODO 先把部门编号暂时存放到部门名称字段里，因为要根据部门编号获取部门Id和name的话涉及跨服务器调用的问题，后面解决
        this.departmentName = objects[6].toString();
    }

}
