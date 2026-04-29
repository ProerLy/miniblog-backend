package com.miniblog.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miniblog.dto.AuthVo;
import com.miniblog.dto.LoginRequest;
import com.miniblog.dto.RegisterRequest;
import com.miniblog.entity.User;
import com.miniblog.mapper.UserMapper;
import com.miniblog.util.JwtUtil;
import com.miniblog.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class AuthService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtUtil jwtUtil;

    public Result<AuthVo> login(LoginRequest req) {
        User user = userMapper.findByUsername(req.getUsername());
        if (user == null) return Result.fail(403, "用户名或密码错误");
        String md5pwd = DigestUtils.md5DigestAsHex(req.getPassword().getBytes());
        if (!md5pwd.equals(user.getPassword())) return Result.fail(403, "用户名或密码错误");
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        AuthVo vo = new AuthVo();
        vo.setToken(token);
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        return Result.ok(vo);
    }

    public Result<AuthVo> register(RegisterRequest req) {
        User exist = userMapper.findByUsername(req.getUsername());
        if (exist != null) return Result.fail(400, "用户名已存在");
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes()));
        user.setNickname(req.getNickname());
        user.setAvatar("https://api.dicebear.com/7.x/avataaars/svg?seed=" + req.getUsername());
        userMapper.insert(user);
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        AuthVo vo = new AuthVo();
        vo.setToken(token);
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setNickname(user.getNickname());
        vo.setAvatar(user.getAvatar());
        return Result.ok(vo);
    }

    public Result<User> getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) return Result.fail(404, "用户不存在");
        user.setPassword(null);
        return Result.ok(user);
    }
}
