package com.mall.controller;

import com.mall.entity.Order;
import com.mall.entity.Result;
import com.mall.service.OrderService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@CrossOrigin
public class OrderController {
    private final OrderService orderService;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private Long getUserId() {
        // 暂时返回1L，生产应从JWT解析
        return 1L;
    }

    /** 从JWT解析用户ID */
    private Long extractUserId(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        try {
            var key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            var claims = Jwts.parser().verifyWith(key).build()
                .parseSignedClaims(authHeader.substring(7)).getPayload();
            return Long.valueOf(claims.getSubject());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 创建订单
     */
    @PostMapping("/create")
    public Result<Order> createOrder(@RequestBody Order order) {
        Order newOrder = orderService.createOrder(
            getUserId(), order.getTotalPrice(),
            order.getReceiverName(), order.getReceiverPhone(), order.getReceiverAddress()
        );
        return Result.ok(newOrder);
    }

    /**
     * 订单列表
     */
    @GetMapping("/list")
    public Result<List<Order>> getOrderList(@RequestParam(required = false) Integer status) {
        List<Order> orders = orderService.getOrderList(getUserId());
        if (status != null) {
            orders = orders.stream().filter(o -> o.getStatus().equals(status)).toList();
        }
        return Result.ok(orders);
    }

    /**
     * 订单详情
     */
    @GetMapping("/detail/{id}")
    public Result<Order> getOrderDetail(@PathVariable Long id) {
        return Result.ok(orderService.getOrderDetail(id, getUserId()));
    }

    /**
     * 通用状态更新
     */
    @PutMapping("/status/{id}")
    public Result<Void> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        orderService.updateOrderStatus(id, getUserId(), status);
        return Result.ok();
    }

    /**
     * 支付回调（Mock）
     * 前端提交支付方式，后端更新状态：待支付(0) -> 待发货(1)
     */
    @PostMapping("/pay")
    public Result<Void> pay(@RequestBody Map<String, Object> params) {
        Long orderId = Long.valueOf(params.get("orderId").toString());
        orderService.updateOrderStatus(orderId, getUserId(), 2); // 0=待支付 -> 2=待发货
        return Result.ok();
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel/{id}")
    public Result<Void> cancelOrder(@PathVariable Long id) {
        orderService.updateOrderStatus(id, getUserId(), 5); // 5=已取消
        return Result.ok();
    }

    /**
     * 确认收货
     */
    @PostMapping("/confirm/{id}")
    public Result<Void> confirmReceive(@PathVariable Long id) {
        orderService.updateOrderStatus(id, getUserId(), 4); // 4=已完成
        return Result.ok();
    }

    /**
     * 提醒发货（Mock，直接返回成功）
     */
    @PostMapping("/remind/{id}")
    public Result<Void> remindShip(@PathVariable Long id) {
        return Result.ok();
    }

    /**
     * 物流信息（Mock）
     */
    @GetMapping("/logistics/{id}")
    public Result<Map<String, Object>> getLogistics(@PathVariable Long id) {
        Map<String, Object> logistics = Map.of(
            "company", "顺丰速运",
            "waybillNo", "SF" + System.currentTimeMillis(),
            "status", "运输中",
            "steps", List.of(
                Map.of("time", "2024-04-13 10:30", "desc", "商品已从深圳发出"),
                Map.of("time", "2024-04-13 14:20", "desc", "快件到达广州中转站"),
                Map.of("time", "2024-04-14 09:00", "desc", "快件正在派送中")
            )
        );
        return Result.ok(logistics);
    }
}
