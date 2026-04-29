package com.miniblog.config;

import com.miniblog.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    public static final String USER_ID = "userId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("[AuthInterceptor] " + request.getMethod() + " " + request.getRequestURI() + " | auth:" + request.getHeader("Authorization"));
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            System.out.println("[AuthInterceptor] token parsed, expired:" + jwtUtil.isExpired(token));
            if (!jwtUtil.isExpired(token)) {
                Long userId = jwtUtil.getUserId(token);
                System.out.println("[AuthInterceptor] userId:" + userId);
                request.setAttribute(USER_ID, userId);
            }
        }
        return true;
    }
}
