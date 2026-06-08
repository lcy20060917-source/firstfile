package com.ecommerce.config.shiro;

import com.ecommerce.common.Constants;
import com.ecommerce.common.JwtUtil;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtFilter extends AccessControlFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtFilter.class);

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = getToken(httpRequest);

        if (token == null) {
            log.debug("Token 缺失: URI={}", httpRequest.getRequestURI());
            return false;
        }

        if (!jwtUtil.validateToken(token)) {
            log.debug("Token 无效或已过期: URI={}", httpRequest.getRequestURI());
            return false;
        }
        Long userId = jwtUtil.getUserId(token);
        String username = jwtUtil.getUsername(token);
        httpRequest.setAttribute(Constants.CURRENT_USER_ATTR, userId);
        httpRequest.setAttribute("currentUsername", username);

        return true;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("{\"code\":401,\"message\":\"Token无效或已过期，请重新登录\",\"data\":null}");
        return false;
    }

    private String getToken(HttpServletRequest request) {
        String header = request.getHeader(Constants.TOKEN_HEADER);
        if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
            return header.substring(Constants.TOKEN_PREFIX.length());
        }
        return null;
    }
}
