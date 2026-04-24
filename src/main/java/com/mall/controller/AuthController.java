package com.mall.controller;

import cn.hutool.core.util.IdUtil;
import com.mall.entity.Result;
import com.mall.entity.User;
import com.mall.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;

    /**
     * 发送验证码（Mock实现，真实项目替换为短信服务商）
     */
    @PostMapping("/sendCode")
    public Result<Void> sendCode(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        if (phone == null || !phone.matches("^1[3-9]\\d{9}$")) {
            return Result.fail(400, "手机号格式错误");
        }
        // Mock: 生成6位验证码，有效期5分钟
        String code = String.valueOf((int) ((Math.random() * 900000) + 100000));
        redisTemplate.opsForValue().set("sms:code:" + phone, code, 5, TimeUnit.MINUTES);
        // 调试用：打印到控制台
        System.out.println("[SMS Mock] 验证码已发送至 " + phone + "，验证码：" + code);
        return Result.ok();
    }

    /**
     * 手机号+验证码登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        String phone = params.get("phone");
        String code = params.get("code");
        if (phone == null || code == null) {
            return Result.fail(400, "参数不完整");
        }
        // 验证码校验（Mock模式：123456 免校验）
        if (!"123456".equals(code)) {
            String storedCode = redisTemplate.opsForValue().get("sms:code:" + phone);
            if (storedCode == null || !storedCode.equals(code)) {
                return Result.fail(401, "验证码错误或已过期");
            }
            redisTemplate.delete("sms:code:" + phone);
        }
        try {
            return Result.ok(userService.loginByPhone(phone));
        } catch (Exception e) {
            return Result.fail(500, "登录失败: " + e.getMessage());
        }
    }
}
