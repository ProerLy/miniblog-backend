package com.mall.service;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.entity.Order;
import com.mall.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(Long userId, BigDecimal totalPrice, String receiverName,
                             String receiverPhone, String receiverAddress) {
        Order order = new Order();
        order.setOrderNo("ORD" + System.currentTimeMillis() + IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase());
        order.setUserId(userId);
        order.setTotalPrice(totalPrice);
        order.setStatus(1); // 1=待付款（前端status含义：1=待付款）
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        orderRepository.insert(order);
        return order;
    }

    public List<Order> getOrderList(Long userId) {
        return orderRepository.selectList(
            new LambdaQueryWrapper<Order>()
                .eq(Order::getUserId, userId)
                .orderByDesc(Order::getCreateTime)
        );
    }

    public Order getOrderDetail(Long orderId, Long userId) {
        return orderRepository.selectOne(
            new LambdaQueryWrapper<Order>()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId)
        );
    }

    public Order getOrderByOrderNo(String orderNo, Long userId) {
        return orderRepository.selectOne(
            new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderNo, orderNo)
                .eq(Order::getUserId, userId)
        );
    }

    public void updateOrderStatus(Long orderId, Long userId, Integer status) {
        Order order = orderRepository.selectOne(
            new LambdaQueryWrapper<Order>()
                .eq(Order::getId, orderId)
                .eq(Order::getUserId, userId)
        );
        if (order != null) {
            order.setStatus(status);
            orderRepository.updateById(order);
        }
    }

    public void updateOrderStatusByOrderNo(String orderNo, Long userId, Integer status) {
        Order order = getOrderByOrderNo(orderNo, userId);
        if (order != null) {
            order.setStatus(status);
            orderRepository.updateById(order);
        }
    }
}
