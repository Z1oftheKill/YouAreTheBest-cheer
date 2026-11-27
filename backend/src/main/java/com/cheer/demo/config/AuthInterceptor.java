package com.cheer.demo.config;

import com.cheer.demo.common.BusinessException;
import com.cheer.demo.common.JwtUtils;
import com.cheer.demo.common.UserContext;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component 
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            throw new BusinessException(401, "请先登录");
        }

        String token = header.substring(7);

        Long userId = JwtUtils.verifyAndGetUserId(token);

        if (userId == null) {
            throw new BusinessException(401, "登录已经过期, 请重新登录");
        }

        UserContext.setUserId(userId);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        UserContext.clear();
    }

}
