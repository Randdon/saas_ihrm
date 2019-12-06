package com.ihrm.system.service;

import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;
import com.zhouyuan.saas.ihrm.utils.BeanMapUtils;
import com.zhouyuan.saas.ihrm.utils.IdWorker;
import com.zhouyuan.saas.ihrm.utils.PermissionConstants;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PermissionService {
    @Autowired
    PermissionDao permissionDao;
    @Autowired
    PermissionApiDao permissionApiDao;
    @Autowired
    PermissionMenuDao permissionMenuDao;
    @Autowired
    PermissionPointDao permissionPointDao;

    @Autowired
    IdWorker idWorker;

    /**
     * 保存权限
     * @param map
     */
    public void add(Map<String, Object> map) throws Exception {
        //设置主键的值
        String id = idWorker.nextId()+"";
        //通过map构造permission对象
        Permission permission = BeanMapUtils.mapToBean(map,Permission.class);
        permission.setId(id);
        //根据类型构造不同的资源对象（菜单、按钮、api）
        Integer type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU :
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map,PermissionMenu.class);
                permissionMenu.setId(id);
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map,PermissionPoint.class);
                permissionPoint.setId(id);
                permissionPointDao.save(permissionPoint);
                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map,PermissionApi.class);
                permissionApi.setId(id);
                permissionApiDao.save(permissionApi);
                break;
        }
        //调用dao保存权限
        permissionDao.save(permission);
    }

    /**
     * 更新权限
     *  1.参数：Permission
     *  2.根据id查询权限对象
     *  3.设置修改的属性
     *  4.调用dao完成更新
     */
    public void update(Map<String,Object> map) throws Exception {
        //通过map构造permission对象
        Permission permission = BeanMapUtils.mapToBean(map,Permission.class);
        //1.根据id查询权限
        Permission target = permissionDao.findById(permission.getId()).orElse(null);
        //2.设置权限属性
        target.setName(permission.getName());
        target.setCode(permission.getCode());
        target.setDescription(permission.getDescription());
        target.setEnVisible(permission.getEnVisible());
        //根据类型构造不同的资源对象（菜单、按钮、api）
        Integer type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU :
                PermissionMenu permissionMenu = BeanMapUtils.mapToBean(map,PermissionMenu.class);
                permissionMenu.setId(permission.getId());
                permissionMenuDao.save(permissionMenu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint permissionPoint = BeanMapUtils.mapToBean(map,PermissionPoint.class);
                permissionPoint.setId(permission.getId());
                permissionPointDao.save(permissionPoint);
                break;
            case PermissionConstants.PY_API:
                PermissionApi permissionApi = BeanMapUtils.mapToBean(map,PermissionApi.class);
                permissionApi.setId(permission.getId());
                permissionApiDao.save(permissionApi);
                break;
        }

        //3.更新权限
        permissionDao.save(target);
    }

    /**
     * 删除权限
     */
    public void deleteById(String id){
        permissionDao.deleteById(id);
    }

    /**
     * 根据id查询权限
     */
    public Permission findById(String id){
        return permissionDao.findById(id).get();
    }

    /**
     * 查询权限列表
     * @return
     */
    public Page<Permission> findAll(Map<String,Object> predicateParams, int page, int size){

        //1. 构造查询条件
        Specification<Permission> specification = new Specification<Permission>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
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
        Page<Permission> pagePermissions = permissionDao.findAll(specification, PageRequest.of(page - 1, size));
        return pagePermissions;
    }

}
