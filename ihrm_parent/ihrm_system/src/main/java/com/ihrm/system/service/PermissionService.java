package com.ihrm.system.service;

import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;
import com.zhouyuan.saas.ihrm.entity.ResultCode;
import com.zhouyuan.saas.ihrm.exception.CommonException;
import com.zhouyuan.saas.ihrm.utils.BeanMapUtils;
import com.zhouyuan.saas.ihrm.utils.IdWorker;
import com.zhouyuan.saas.ihrm.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional //因为很多方法中都是涉及多个表的操作，所以加上该事务注解
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
            default:
                throw new CommonException(ResultCode.FAIL);
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
            default:
                throw new CommonException(ResultCode.FAIL);
        }

        //3.更新权限
        permissionDao.save(target);
    }

    /**
     * 删除权限
     */
    public void deleteById(String id) throws CommonException {
        Permission permission = permissionDao.findById(id).get();
        permissionDao.delete(permission);
        Integer type = permission.getType();
        switch (type){
            case PermissionConstants.PY_MENU :
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
    }

    /**
     * 根据id查询权限
     * @return
     */
    public Map<String, Object> findById(String id) throws CommonException {
        Permission permission = permissionDao.findById(id).get();
        Integer type = permission.getType();

        Object resourcePerm = null;
        switch (type){
            case PermissionConstants.PY_MENU :
                resourcePerm = permissionMenuDao.findById(id);
                break;
            case PermissionConstants.PY_POINT:
                resourcePerm = permissionPointDao.findById(id);
                break;
            case PermissionConstants.PY_API:
                resourcePerm = permissionApiDao.findById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }

        Map<String,Object> result = BeanMapUtils.beanToMap(resourcePerm);
        Map<String, Object> permssionMap = BeanMapUtils.beanToMap(permission);
        result.putAll(permssionMap);
        return result;
    }

    /**
     * 查询权限列表
     * type      : 查询全部权限列表type：0：菜单 + 按钮（权限点） 1：菜单2：按钮（权限点）3：API接口
     * enVisible : 0：查询所有saas平台的最高权限，1：查询企业的权限
     * pid ：父id
     * @return
     */
    public List<Permission> findAll(Map<String,Object> predicateParams){

        //1. 构造查询条件
        Specification<Permission> specification = new Specification<Permission>() {
            @Override
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>(predicateParams.size());
                //根据请求的父id构造查询条件
                if (!StringUtils.isEmpty(predicateParams.get("pid"))){
                    predicates.add(criteriaBuilder.equal(root.get("pid").as(String.class),predicateParams.get("pid")));
                }
                //根据请求的enVisible构造查询条件
                if (!StringUtils.isEmpty(predicateParams.get("enVisible"))){
                    predicates.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),predicateParams.get("enVisible")));
                }
                //根据权限类型构造查询条件
                if(!StringUtils.isEmpty(predicateParams.get("type"))) {
                    String type = predicateParams.get("type").toString();
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if("0".equals(type)) {
                        in.value(1).value(2);
                    }else {
                        in.value(Integer.parseInt(type));
                    }
                    predicates.add(in);
                }
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };

        //2.数据库查询
        List<Permission> permissions = permissionDao.findAll(specification);
        return permissions;
    }

}
