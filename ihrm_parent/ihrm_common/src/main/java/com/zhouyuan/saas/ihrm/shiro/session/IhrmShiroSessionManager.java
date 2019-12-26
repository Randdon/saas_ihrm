package com.zhouyuan.saas.ihrm.shiro.session;

import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 * @description: shiro统一会话管理器
 * @author: yuand
 * @create: 2019-12-26 11:06
 **/
public class IhrmShiroSessionManager extends DefaultWebSessionManager {

    /**
     * 指定获取sessionId的方式：和前台约定，在请求头中加入Authorization字段，放入Bearer +sessionId
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
        String authorization = WebUtils.toHttp(request).getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)){
            //如果前台没有携带sessionId，则生成一个新的sessionId返回
            return super.getSessionId(request, response);
        }
        String sessionId = authorization.replace("Bearer ", "");
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, "header");
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, sessionId);
        request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
        return sessionId;
    }
}
