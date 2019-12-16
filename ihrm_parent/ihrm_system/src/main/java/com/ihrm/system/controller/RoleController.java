package com.ihrm.system.controller;

import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.response.RoleResult;
import com.ihrm.system.service.RoleService;
import com.zhouyuan.saas.ihrm.controller.BaseController;
import com.zhouyuan.saas.ihrm.entity.PageResult;
import com.zhouyuan.saas.ihrm.entity.Result;
import com.zhouyuan.saas.ihrm.entity.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//1.解决跨域
@CrossOrigin
//2.声明restContoller
@RestController
//3.设置父路径
@RequestMapping(value="/sys")
public class RoleController extends BaseController {

    @Autowired
    private RoleService roleService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleController.class);

    /**
     * 分配权限
     * @param map
     * @return
     */
    @RequestMapping(value = "/role/assignPerms",method = RequestMethod.PUT)
    public Result assignRoles(@RequestBody Map<String,Object> map){
        //1.获取被分配的角色id
        String roleId = map.get("id").toString();
        //2.获取到权限的id列表
        List<String> permIds = (List<String>) map.get("permIds");
        //3.调用service完成角色分配
        roleService.assignRoles(roleId,permIds);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 保存
     */
    @RequestMapping(value="/role",method = RequestMethod.POST)
    public Result save(@RequestBody Role role) {
        //1.设置保存的企业id

        role.setCompanyId(companyId);
        //2.调用service完成保存角色
        roleService.add(role);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的角色列表
     * 指定企业id
     */
    @RequestMapping(value="/role",method = RequestMethod.GET)
    public Result findAll(int pagesize, int page, @RequestParam Map map) {
        map.put("companyId",companyId);
        Page roles = roleService.findByPage(companyId,page,pagesize);
        //构造返回结果
        PageResult pageResult = new PageResult(roles.getTotalElements(),roles.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    /**
     * 根据ID查询role
     */
    @RequestMapping(value="/role/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value="id") String id) {
        Role role = roleService.findById(id);
        //在回显角色时返回该角色已有的权限id组合（前台也要改）
        //TODO 问题：在此处没有加事务注解的情况下，查询用户时也能获取到和用户关联的角色集合，但是在单元测试中，不加事务注解会报懒加载异常
        RoleResult roleResult = new RoleResult(role);
        return new Result(ResultCode.SUCCESS,roleResult);
    }

    @RequestMapping(value="/role/list" ,method=RequestMethod.GET)
    public Result findAll() {
        List<Role> roleList = roleService.findAll(companyId);
        return new Result(ResultCode.SUCCESS,roleList);
    }


    /**
     * 修改Role
     */
    @RequestMapping(value="/role/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id, @RequestBody Role role) {
        //1.设置修改的部门id
        role.setId(id);
        //2.调用service更新
        roleService.update(role);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value="/role/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        roleService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }


}
