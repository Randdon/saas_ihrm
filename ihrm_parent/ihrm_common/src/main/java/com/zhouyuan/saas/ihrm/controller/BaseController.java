package com.zhouyuan.saas.ihrm.controller;

import io.jsonwebtoken.Claims;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BaseController {

    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected String companyId;
    protected String companyName;
    //token解析结果
    protected Claims claims;

    /**
     * 被@ModelAttribute注释的方法会在此controller每个方法执行前被执行
     * 因为很多Controller的方法都要用到CompanyId当作参数，所以提取到公共controller里
     * @param request
     * @param response
     */
    @ModelAttribute
    public void setRequestAndResponse(HttpServletRequest request,HttpServletResponse response){
        this.request = request;
        this.response = response;
        Object obj = request.getAttribute("user_claims");

        if (null != obj){
            this.claims = (Claims) obj;

            /**
             * 从token中获取companyId和companyName
             */
            this.companyId = claims.get("companyId",String.class);
            this.companyName = claims.get("companyName",String.class);
        }
    }
}
