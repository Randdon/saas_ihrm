package com.ihrm.system.controller;

import com.ihrm.system.DepartmentFeignClient;
import com.zhouyuan.saas.ihrm.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(value="/sys")
public class FeignTestController {

    @Autowired
    DepartmentFeignClient departmentFeignClient;

    @RequestMapping(value = "testFeign/{id}", method = RequestMethod.GET)
    public Result testFeign(@PathVariable(value = "id") String id){
        Result result = departmentFeignClient.findById(id);
        return result;
    }
}
