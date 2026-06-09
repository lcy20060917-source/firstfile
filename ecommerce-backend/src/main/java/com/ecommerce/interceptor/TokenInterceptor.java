package com.ecommerce.interceptor;

import com.ecommerce.common.Constants;
import com.ecommerce.common.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(TokenInterceptor.class);
    private final JwtUtil jwtUtil;

    public TokenInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        log.info(">>> {} {}", method, uri);

        String path = request.getRequestURI();
        if (path.startsWith("/api/auth/") || path.startsWith("/api/products")) {
            String token = extractToken(request);
            if (token != null && jwtUtil.validateToken(token)) {
                setUser(request, token);
            }
            return true;
        }

        String token = extractToken(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            response.setContentType("application/json;charset=UTF-8");
            response.setStatus(401);
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或Token已过期\"}");
            return false;
        }
        setUser(request, token);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                 Object handler, Exception ex) {
        log.info("<<< {} {} status={}", request.getMethod(), request.getRequestURI(),
                response.getStatus());
    }

    private void setUser(HttpServletRequest request, String token) {
        request.setAttribute(Constants.CURRENT_USER_ATTR, jwtUtil.getUserId(token));
    }

    private String extractToken(HttpServletRequest request) {
        String header = request.getHeader(Constants.TOKEN_HEADER);
        if (header != null && header.startsWith(Constants.TOKEN_PREFIX)) {
            return header.substring(Constants.TOKEN_PREFIX.length());
        }
        return null;
    }
}
