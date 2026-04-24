package com.mall.controller;

import com.mall.entity.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {
    private final UserService userService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody User user) {
        try {
            return Result.ok(userService.login(user.getUsername(), user.getPassword()));
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @PostMapping("/register")
    public Result<User> register(@RequestBody User user) {
        try {
            return Result.ok(userService.register(user));
        } catch (Exception e) {
            return Result.fail(e.getMessage());
        }
    }

    @GetMapping("/info/{id}")
    public Result<User> getUserInfo(@PathVariable Long id) {
        return Result.ok(userService.getUserInfo(id));
    }

    @PutMapping("/update")
    public Result<Void> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return Result.ok();
    }

    /**
     * 校验Token有效性
     */
    @GetMapping("/checkToken")
    public Result<Map<String, Object>> checkToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Result.fail(401, "未登录");
        }
        String token = authHeader.substring(7);
        Map<String, Object> userInfo = userService.checkToken(token);
        if (userInfo == null) {
            return Result.fail(401, "Token无效或已过期");
        }
        return Result.ok(userInfo);
    }

    /**
     * 当前用户各状态订单数量
     */
    @GetMapping("/orderCounts")
    public Result<Map<String, Integer>> getOrderCounts(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        Long userId = extractUserId(authHeader);
        if (userId == null) {
            return Result.fail(401, "未登录");
        }
        return Result.ok(userService.getOrderCounts(userId));
    }

    private Long extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        try {
            var key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(authHeader.substring(7)).getPayload();
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }
}
