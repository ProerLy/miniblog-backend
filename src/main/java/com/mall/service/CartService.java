package com.mall.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.entity.Cart;
import com.mall.entity.CartGroup;
import com.mall.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;

    public List<CartGroup> getCartGroupList(Long userId) {
        List<Cart> carts = cartRepository.selectList(
            new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .orderByDesc(Cart::getCreateTime)
        );
        // 按 shopId 分组
        Map<Long, List<Cart>> groupMap = new LinkedHashMap<>();
        for (Cart cart : carts) {
            Long shopId = cart.getShopId() != null ? cart.getShopId() : 0L;
            groupMap.computeIfAbsent(shopId, k -> new ArrayList<>()).add(cart);
        }
        return groupMap.entrySet().stream().map(entry -> {
            CartGroup group = new CartGroup();
            group.setShopId(entry.getKey());
            String shopName = entry.getValue().get(0).getShopName();
            group.setShopName(shopName != null ? shopName : "商城自营");
            List<CartGroup.CartItem> items = entry.getValue().stream().map(cart -> {
                CartGroup.CartItem item = new CartGroup.CartItem();
                item.setId(cart.getId());
                item.setGoodsId(cart.getGoodsId());
                item.setName(cart.getGoodsName());
                item.setPic(cart.getGoodsImage());
                item.setSkuText(cart.getSku());
                item.setPrice(cart.getPrice());
                item.setNum(cart.getQuantity());
                item.setSelected(false);
                return item;
            }).collect(Collectors.toList());
            group.setGoods(items);
            group.setAllSelected(false);
            return group;
        }).collect(Collectors.toList());
    }

    public void addToCart(Cart cart) {
        // 查重（同店铺+同商品+同规格）
        LambdaQueryWrapper<Cart> qw = new LambdaQueryWrapper<Cart>()
            .eq(Cart::getUserId, cart.getUserId())
            .eq(Cart::getGoodsId, cart.getGoodsId())
            .eq(cart.getSku() != null, Cart::getSku, cart.getSku());
        Cart exist = cartRepository.selectOne(qw);
        if (exist != null) {
            exist.setQuantity(exist.getQuantity() + cart.getQuantity());
            cartRepository.updateById(exist);
        } else {
            cartRepository.insert(cart);
        }
    }

    public void updateQuantity(Long cartId, Integer quantity) {
        if (quantity != null && quantity <= 0) {
            cartRepository.deleteById(cartId);
        } else {
            Cart cart = new Cart();
            cart.setId(cartId);
            cart.setQuantity(quantity);
            cartRepository.updateById(cart);
        }
    }

    public void removeCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }

    public void batchRemove(List<Long> ids) {
        if (ids != null && !ids.isEmpty()) {
            cartRepository.deleteBatchIds(ids);
        }
    }

    public void clearCart(Long userId) {
        cartRepository.delete(new LambdaQueryWrapper<Cart>().eq(Cart::getUserId, userId));
    }

    /**
     * 获取当前用户购物车中指定商品的总数量
     */
    public int getGoodsCount(Long userId, Long goodsId) {
        List<Cart> carts = cartRepository.selectList(
            new LambdaQueryWrapper<Cart>()
                .eq(Cart::getUserId, userId)
                .eq(Cart::getGoodsId, goodsId)
        );
        return carts.stream()
                .mapToInt(c -> c.getQuantity() != null ? c.getQuantity() : 0)
                .sum();
    }
}
