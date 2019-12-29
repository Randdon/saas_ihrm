package com.ihrm.system.controller;

import com.ihrm.system.DepartmentFeignClient;
import com.zhouyuan.saas.ihrm.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Feign测试controller
 */
@RestController
@CrossOrigin
@RequestMapping(value="/sys")
public class FeignTestController {

    @Autowired
    DepartmentFeignClient departmentFeignClient;

    @RequestMapping(value = "testFeign/{id}", method = RequestMethod.GET)
    public Result testFeign(@PathVariable(value = "id") String id){
        //微服务调用组件Feign会将该请求发送往企业微服务http://ihrm-company/department/1063676045212913664
        Result result = departmentFeignClient.findById(id);
        return result;
    }
}
