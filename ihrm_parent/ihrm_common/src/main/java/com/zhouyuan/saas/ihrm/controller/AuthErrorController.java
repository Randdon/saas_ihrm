package com.zhouyuan.saas.ihrm.controller;

import com.zhouyuan.saas.ihrm.entity.Result;
import com.zhouyuan.saas.ihrm.entity.ResultCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: shiro认证或鉴权失败controller
 * @author: yuand
 * @create: 2019-12-26 15:40
 **/
@RestController
@CrossOrigin
public class AuthErrorController {

    /**
     * 公共错误跳转
     * @param code
     * @return
     */
    @RequestMapping(value = "/autherror")
    public Result autherror(int code){
        return code == 1 ? new Result(ResultCode.UNAUTHENTICATED) : new Result(ResultCode.UNAUTHORISE);
    }
}
