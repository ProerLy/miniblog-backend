package com.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.entity.Order;
import com.mall.entity.User;
import com.mall.repository.OrderRepository;
import com.mall.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.ttl}")
    private Long jwtTtl;

    public Map<String, Object> login(String username, String password) {
        User user = userRepository.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username)
                .eq(User::getPassword, password)
        );
        if (user == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        String token = generateToken(user);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return result;
    }

    public User register(User user) {
        User exist = userRepository.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername())
        );
        if (exist != null) {
            throw new RuntimeException("用户名已存在");
        }
        userRepository.insert(user);
        return user;
    }

    public User getUserInfo(Long userId) {
        return userRepository.selectById(userId);
    }

    public void updateUser(User user) {
        userRepository.updateById(user);
    }

    private String generateToken(User user) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
            .subject(user.getId().toString())
            .claim("username", user.getUsername())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + jwtTtl))
            .signWith(key)
            .compact();
    }

    /**
     * 手机号+验证码登录（自动注册）
     */
    public Map<String, Object> loginByPhone(String phone) {
        User user = userRepository.selectOne(
            new LambdaQueryWrapper<User>().eq(User::getPhone, phone)
        );
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setUsername("u_" + phone); // username非空，设为u_手机号
            user.setPassword("123456"); // 验证码登录默认密码
            user.setNickname("用户" + phone.substring(phone.length() - 4));
            user.setAvatar("https://img.yzcdn.cn/vant/cat.jpeg");
            userRepository.insert(user);
        }
        String token = generateToken(user);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("nickname", user.getNickname());
        userInfo.put("avatar", user.getAvatar());
        userInfo.put("phone", user.getPhone());
        userInfo.put("vipLevel", 0);
        result.put("userInfo", userInfo);
        return result;
    }

    /**
     * 校验Token，返回用户信息
     */
    public Map<String, Object> checkToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            Long userId = Long.valueOf(claims.getSubject());
            User user = userRepository.selectById(userId);
            if (user == null) return null;
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("avatar", user.getAvatar());
            userInfo.put("phone", user.getPhone());
            userInfo.put("vipLevel", 0);
            return userInfo;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 各状态订单数量统计
     * 0-待支付 1-待发货 2-待收货 3-待评价 4-已完成 5-已取消
     */
    public Map<String, Integer> getOrderCounts(Long userId) {
        List<Order> orders = orderRepository.selectList(
            new LambdaQueryWrapper<Order>().eq(Order::getUserId, userId)
        );
        Map<String, Integer> counts = new HashMap<>();
        counts.put("waitPay", 0);
        counts.put("waitShip", 0);
        counts.put("waitReceive", 0);
        counts.put("waitComment", 0);
        for (Order order : orders) {
            switch (order.getStatus()) {
                case 0: counts.put("waitPay", counts.get("waitPay") + 1); break;
                case 1: counts.put("waitShip", counts.get("waitShip") + 1); break;
                case 2: counts.put("waitReceive", counts.get("waitReceive") + 1); break;
                case 3: counts.put("waitComment", counts.get("waitComment") + 1); break;
            }
        }
        return counts;
    }
}
