package com.ihrm.system.controller;

import com.ihrm.domain.system.Permission;
import com.ihrm.system.service.PermissionService;
import com.zhouyuan.saas.ihrm.controller.BaseController;
import com.zhouyuan.saas.ihrm.entity.PageResult;
import com.zhouyuan.saas.ihrm.entity.Result;
import com.zhouyuan.saas.ihrm.entity.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

//1.解决跨域
@CrossOrigin
//2.声明restContoller
@RestController
//3.设置父路径
@RequestMapping(value="/sys")
public class PermissionController extends BaseController {

    @Autowired
    private PermissionService permissionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionController.class);

    /**
     * 保存
     */
    @RequestMapping(value="/permission",method = RequestMethod.POST)
    public Result save(@RequestBody Map<String,Object> map) {
        //1.设置保存的权限id

        //2.调用service完成保存权限
        try {
            permissionService.add(map);
        } catch (Exception e) {
            LOGGER.error("权限保存失败：{}",e);
        }
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询权限的部门列表
     * 指定权限id
     */
    @RequestMapping(value="/permission",method = RequestMethod.GET)
    public Result findAll(int size, int page, @RequestParam Map map) {
        map.put("companyId",companyId);
        Page permissions = permissionService.findAll(map,page,size);
        //构造返回结果
        PageResult pageResult = new PageResult(permissions.getTotalElements(),permissions.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    /**
     * 根据ID查询permission
     */
    @RequestMapping(value="/permission/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value="id") String id) {
        Permission permission = permissionService.findById(id);
        return new Result(ResultCode.SUCCESS,permission);
    }

    /**
     * 修改Permission
     */
    @RequestMapping(value="/permission/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id, @RequestParam Map<String,Object> map) {
        //1.设置修改的权限id
        map.put("id",id);
        //2.调用service更新
        permissionService.update(map);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value="/permission/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        permissionService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }


}
