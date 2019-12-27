package com.zhouyuan.saas.ihrm.handler;

import com.zhouyuan.saas.ihrm.entity.Result;
import com.zhouyuan.saas.ihrm.entity.ResultCode;
import com.zhouyuan.saas.ihrm.exception.CommonException;
import org.apache.shiro.authz.AuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @description: 自定义公共异常处理器
 * @author: yuand
 * @create: 2019-12-17 14:01
 **/
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception exception){
        exception.printStackTrace();
        if(exception.getClass() == CommonException.class) {
            //类型转型
            CommonException ce = (CommonException) exception;
            Result result = new Result(ce.getResultCode());
            return result;
        }else{
            Result result = new Result(ResultCode.SERVER_ERROR);
            return result;
        }
    }

    @ExceptionHandler(value = AuthorizationException.class)
    @ResponseBody
    public Result authorizeError(AuthorizationException exp){
        exp.printStackTrace();
        return new Result(ResultCode.UNAUTHORISE);
    }
}
