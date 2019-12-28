package com.ihrm.system;

import com.zhouyuan.saas.ihrm.entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Feign-在微服务调用方创建被调用微服务的Java接口
 */
@FeignClient(value = "ihrm-company")//该注解用于声明被调用微服务的名称
public interface DepartmentFeignClient {
    /**
     * 创建需要被调用的微服务里的具体方法，和被调用方的controller方法的RequestMapping和方法签名一样
     * 见com.ihrm.company.controller.DepartmentController#findById(java.lang.String)
     * @param id
     * @return
     */
    @RequestMapping(value="/department/{id}",method = RequestMethod.GET)
    Result findById(@PathVariable(value="id") String id);
}
