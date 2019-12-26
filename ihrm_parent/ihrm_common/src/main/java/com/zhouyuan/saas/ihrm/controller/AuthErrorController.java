package com.zhouyuan.saas.ihrm.controller;

import com.zhouyuan.saas.ihrm.entity.Result;
import com.zhouyuan.saas.ihrm.entity.ResultCode;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @description: shiro认证或鉴权失败controller
 * @author: yuand
 * @create: 2019-12-26 15:40
 **/
@Controller
public class AuthErrorController {

    @RequestMapping(value = "/autherror")
    public Result autherror(int code){
        return code == 1 ? new Result(ResultCode.UNAUTHENTICATED) : new Result(ResultCode.UNAUTHORISE);
    }
}
