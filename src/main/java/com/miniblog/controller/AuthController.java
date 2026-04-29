package com.miniblog.controller;

import com.miniblog.dto.LoginRequest;
import com.miniblog.dto.RegisterRequest;
import com.miniblog.dto.AuthVo;
import com.miniblog.entity.User;
import com.miniblog.service.AuthService;
import com.miniblog.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Result<AuthVo> login(@RequestBody LoginRequest req) {
        return authService.login(req);
    }

    @PostMapping("/register")
    public Result<AuthVo> register(@RequestBody RegisterRequest req) {
        return authService.register(req);
    }

    @GetMapping("/userinfo")
    public Result<User> userinfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) return Result.fail(401, "未登录");
        return authService.getUserInfo(userId);
    }
}
