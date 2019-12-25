package com.zhouyuan.shiro.session;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @description: 自定义session管理器——用以整合shiro和redis实现session的统一管理，以支持分布式架构
 * @author: yuand
 * @create: 2019-12-25 15:36
 **/
public class CustomSessionManager extends DefaultWebSessionManager {

    /**
     * 指定获取sessionId的方式：和前台约定，在请求头中加入Authorization字段，放入sessionId
     * @param request
     * @param response
     * @return
     */
    @Override
    protected Serializable getSessionId(ServletRequest request, ServletResponse response) {
        /**
         * 因为这里的request参数是ServletRequest，并不一定就是HttpServletRequest，
         * 所以利用shiro提供的WebUtils将request转为HttpServletRequest
         * 然后获取请求头Authorization中的数据
         */
        String id = WebUtils.toHttp(request).getHeader("Authorization");

        if (StringUtils.isEmpty(id)){
            //如果前台没有携带sessionId，则生成一个新的sessionId返回
            return super.getSessionId(request, response);
        }
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, id);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        return id;
    }
}
