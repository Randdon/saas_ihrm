package com.zhouyuan.shiro.handler;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 统一异常处理类
 * @author: yuand
 * @create: 2019-12-25 10:51
 **/
@ControllerAdvice
public class BaseExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = AuthorizationException.class)
    public String handle(Exception ex){
        ex.printStackTrace();
        return "未授权！";
    }
}
