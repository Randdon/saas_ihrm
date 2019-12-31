package com.ihrm.system.service;

import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import com.zhouyuan.saas.ihrm.utils.IdWorker;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    @Autowired
    IdWorker idWorker;
    @Autowired
    RoleDao roleDao;

    /**
     * 保存用户
     * @param user
     */
    public void add(User user){
        //设置主键的值
        String id = idWorker.nextId()+"";
        user.setId(id);
        //设置默认密码
        user.setPassword(new Md5Hash("123456",user.getMobile(),3).toString());
        //基本初始属性
        user.setLevel("user");
        user.setInServiceStatus(1);
        user.setEnableState(1);
        //调用dao保存用户
        userDao.save(user);
    }

    /**
     * 更新用户
     *  1.参数：User
     *  2.根据id查询用户对象
     *  3.设置修改的属性
     *  4.调用dao完成更新
     */
    public void update(User user){
        //1.根据id查询用户
        User target = userDao.findById(user.getId()).get();
        //2.设置用户属性
        target.setUsername(user.getUsername());
        target.setPassword(user.getPassword());
        target.setDepartmentId(user.getDepartmentId());
        target.setDepartmentName(user.getDepartmentName());
        //3.更新用户
        userDao.save(target);
    }

    /**
     * 删除用户
     */
    public void deleteById(String id){
        userDao.deleteById(id);
    }

    /**
     * 根据id查询用户
     */
    public User findById(String id){
        return userDao.findById(id).get();
    }

    /**
     * 查询用户列表
     * @return
     */
    public Page<User> findAll(Map<String,Object> predicateParams, int page, int size){

        //1. 构造查询条件
        Specification<User> specification = new Specification<User>() {
            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>(predicateParams.size());
                //根据请求的companyId构造查询条件
                if (!StringUtils.isEmpty(predicateParams.get("companyId"))){
                    predicates.add(criteriaBuilder.equal(root.get("companyId").as(String.class),predicateParams.get("companyId")));
                }
                //根据请求的部门id构造查询条件
                if (!StringUtils.isEmpty(predicateParams.get("departmentId"))){
                    predicates.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),predicateParams.get("departmentId")));
                }
                //根据是否分配部门构造查询条件
                if(!StringUtils.isEmpty(predicateParams.get("hasDept"))) {
                    //根据请求的hasDept判断  是否分配部门 0未分配（departmentId = null），1 已分配 （departmentId ！= null）
                    if("0".equals((String) predicateParams.get("hasDept"))) {
                        predicates.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }else {
                        predicates.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        //2.分页 page-1是因为jpa的分页是从0开始的
        Page<User> pageUsers = userDao.findAll(specification, PageRequest.of(page - 1, size));
        return pageUsers;
    }

    /**
     * 为用户分配角色
     * @param userId
     * @param roleIds
     */
    public void assignRoles(String userId, List<String> roleIds) {
        //1.根据id查询用户
        User user = userDao.findById(userId).get();
        //2.设置用户的角色集合
        Set<Role> roles = roleDao.findAllById(roleIds).stream().collect(Collectors.toSet());
        //设置用户和角色集合的关系
        user.setRoles(roles);
        //3.更新用户：如果userid是表中没有的，则会入库bs_user和pe_user_role两张表，如果userid已存在，则只入库pe_user_role表
        userDao.save(user);
    }

    /**
     * 根据电话号码查询用户
     * @param mobile
     * @return
     */
    public User findByMobile(String mobile){
        User user = userDao.findByMobile(mobile);
        return user;
    }

    /**
     * 批量保存用户
     * @param users
     * @param companyId
     * @param companyName
     */
    public void saveAll(List<User> users, String companyId, String companyName) {

        users.forEach(user -> {
            //生成主键id
            user.setId(idWorker.nextId() + "");
            //设置默认密码
            user.setPassword(new Md5Hash("123456",user.getMobile(),3).toString());
            //基本初始属性
            user.setCompanyName(companyName);
            user.setCompanyId(companyId);
            user.setLevel("user");
            user.setInServiceStatus(1);
            user.setEnableState(1);
            user.setCreateTime(new Date());
        });
        userDao.saveAll(users);
    }
}
