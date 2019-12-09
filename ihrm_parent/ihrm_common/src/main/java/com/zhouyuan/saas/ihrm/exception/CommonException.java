package com.zhouyuan.saas.ihrm.exception;

import com.zhouyuan.saas.ihrm.entity.ResultCode;

/**
 * 自定义异常
 */
public class CommonException extends Exception  {

    private ResultCode resultCode;

    public CommonException(ResultCode resultCode) {
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
