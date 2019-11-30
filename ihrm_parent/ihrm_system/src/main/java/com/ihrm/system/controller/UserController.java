package com.ihrm.system.controller;

import com.ihrm.domain.system.User;
import com.ihrm.system.service.UserService;
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
@RequestMapping(value="/sys")   //  company/deparment
public class UserController extends BaseController {

    @Autowired
    private UserService userService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * 保存
     */
    @RequestMapping(value="/user",method = RequestMethod.POST)
    public Result save(@RequestBody User user) {
        //1.设置保存的企业id

        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        user.setCreateTime(new Date());
        //2.调用service完成保存企业
        userService.add(user);
        //3.构造返回结果
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 查询企业的部门列表
     * 指定企业id
     */
    @RequestMapping(value="/user",method = RequestMethod.GET)
    public Result findAll(int size, int page, @RequestParam Map map) {
        map.put("companyId",companyId);
        Page users = userService.findAll(map,page,size);
        //构造返回结果
        PageResult pageResult = new PageResult(users.getTotalElements(),users.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }

    /**
     * 根据ID查询user
     */
    @RequestMapping(value="/user/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value="id") String id) {
        User user = userService.findById(id);
        return new Result(ResultCode.SUCCESS,user);
    }

    /**
     * 修改User
     */
    @RequestMapping(value="/user/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value="id") String id, @RequestBody User user) {
        //1.设置修改的部门id
        user.setId(id);
        //2.调用service更新
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }

    /**
     * 根据id删除
     */
    @RequestMapping(value="/user/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value="id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }


}
