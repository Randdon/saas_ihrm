package com.zhouyuan.saas.ihrm.interceptor;

import com.zhouyuan.saas.ihrm.entity.ResultCode;
import com.zhouyuan.saas.ihrm.exception.CommonException;
import com.zhouyuan.saas.ihrm.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description: jwt拦截器
 * 自定义拦截器
 *      继承HandlerInterceptorAdapter
 *
 *      preHandle:进入到控制器方法之前执行的内容
 *          boolean：
 *              true：可以继续执行控制器方法
 *              false：拦截
 *      posthandler：执行控制器方法之后执行的内容
 *      afterCompletion：响应结束之前执行的内容
 *
 * 1.简化获取token数据的代码编写
 *      统一的用户权限校验（是否登录）
 * 2.判断用户是否具有当前访问接口的权限
 *
 * @author: yuand
 * @create: 2019-12-17 17:04
 **/
@Component
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    JwtUtils jwtUtils;

    /**
     * 简化获取token数据的代码编写（判断是否登录）
     *  1.通过request获取请求token信息
     *  2.从token中解析获取claims
     *  3.将claims绑定到request域中
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.通过request获取请求token信息
        String authorization = request.getHeader("Authorization");
        //判断请求头信息是否为空，并且是否按约定以Bearer开头
        if (!StringUtils.isEmpty(authorization) && authorization.startsWith("Bearer")){
            //获取token数据
            String token = authorization.replace("Bearer ", "");
            //解析token获取claims
            Claims claims = jwtUtils.parseJwt(token);
            if (null != claims){
                //将claims绑定到request域中
                request.setAttribute("user_claims",claims);
                return true;
            }
        }
        throw new CommonException(ResultCode.UNAUTHENTICATED);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
    }
}
