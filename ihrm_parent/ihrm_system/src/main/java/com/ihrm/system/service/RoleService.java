package com.ihrm.system.service;

import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import com.zhouyuan.saas.ihrm.service.BaseService;
import com.zhouyuan.saas.ihrm.utils.IdWorker;
import com.zhouyuan.saas.ihrm.utils.PermissionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleService extends BaseService {
    @Autowired
    RoleDao roleDao;
    @Autowired
    IdWorker idWorker;
    @Autowired
    PermissionDao permissionDao;

    /**
     * 保存角色
     * @param role
     */
    public void add(Role role){
        //设置主键的值
        String id = idWorker.nextId()+"";
        role.setId(id);
        //调用dao保存角色
        roleDao.save(role);
    }

    /**
     * 更新角色
     *  1.参数：Role
     *  2.根据id查询角色对象
     *  3.设置修改的属性
     *  4.调用dao完成更新
     */
    public void update(Role role){
        //1.根据id查询角色
        Role target = roleDao.findById(role.getId()).get();
        //2.设置角色属性
        target.setName(role.getName());
        target.setDescription(role.getDescription());
        //3.更新角色
        roleDao.save(target);
    }

    /**
     * 删除角色
     */
    public void deleteById(String id){
        roleDao.deleteById(id);
    }

    /**
     * 根据id查询角色
     */
    public Role findById(String id){
        return roleDao.findById(id).get();
    }

    public List<Role> findAll(String companyId) {
        return roleDao.findAll(getSpecification(companyId));
    }
    /**
     * 分页查询角色列表
     * @return
     */
    public Page<Role> findByPage(String companyId, int page, int size){
        return roleDao.findAll(getSpecification(companyId),PageRequest.of(page-1,size));
    }

    /**
     * 为角色分配权限
     * @param roleId
     * @param permissionIds
     */
    public void assignRoles(String roleId, List<String> permissionIds) {
        //1.根据id查询角色
        Role role = roleDao.findById(roleId).get();
        //2.查找权限列表
        Set<Permission> permissions = permissionDao.findAllById(permissionIds).stream().collect(Collectors.toSet());
        List<Permission> childApiPerms = new ArrayList<>(16);
        permissions.stream().forEach(permission -> {
            //查找每个权限所对应的子API权限
            childApiPerms.addAll(permissionDao.findByTypeAndPid(PermissionConstants.PY_API,permission.getId()));
        });
        permissions.addAll(childApiPerms.stream().collect(Collectors.toSet()));
        //设置角色和权限集合的关系
        role.setPermissions(permissions);
        //3.更新角色：如果roleid是表中没有的，则会入库bs_role和pe_role_permission两张表，如果roleid已存在，则只入库pe_role_permission表
        roleDao.save(role);
    }
}
