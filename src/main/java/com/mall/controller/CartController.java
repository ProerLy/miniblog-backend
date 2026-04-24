package com.mall.controller;

import com.mall.entity.Cart;
import com.mall.entity.CartGroup;
import com.mall.entity.Goods;
import com.mall.entity.Result;
import com.mall.repository.GoodsRepository;
import com.mall.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@CrossOrigin
public class CartController {
    private final CartService cartService;
    private final GoodsRepository goodsRepository;

    private Long getUserId() {
        // 临时返回1L，后续接入JWT认证后替换
        return 1L;
    }

    /**
     * 购物车列表（按店铺分组）
     * 前端期望: [{ shopId, shopName, allSelected, goods: [{id, goodsId, name, pic, skuText, price, num, selected}] }]
     */
    @GetMapping("/list")
    public Result<List<CartGroup>> getCartList() {
        return Result.ok(cartService.getCartGroupList(getUserId()));
    }

    /**
     * 添加购物车
     * goodsId, num, sku(可选) 必填；shopId/shopName 可选（自动取goods的）
     */
    @PostMapping("/add")
    public Result<Void> addToCart(@RequestBody Cart cart) {
        try {
        cart.setUserId(getUserId());
        // 自动补充商品信息
        Goods goods = goodsRepository.selectById(cart.getGoodsId());
        if (goods != null) {
            if (cart.getGoodsName() == null) cart.setGoodsName(goods.getName());
            if (cart.getGoodsImage() == null) cart.setGoodsImage(goods.getMainImage());
            if (cart.getPrice() == null) cart.setPrice(goods.getPrice());
            // 默认店铺
            if (cart.getShopId() == null) cart.setShopId(1L);
            if (cart.getShopName() == null) cart.setShopName("商城自营");
        }
        cartService.addToCart(cart);
        return Result.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail(500, "添加失败: " + e.getMessage());
        }
    }

    /**
     * 更新商品数量
     */
    @PutMapping("/update")
    public Result<Void> updateQuantity(@RequestBody Cart cart) {
        cartService.updateQuantity(cart.getId(), cart.getQuantity());
        return Result.ok();
    }

    /**
     * 删除单个购物车商品
     */
    @DeleteMapping("/remove/{id}")
    public Result<Void> removeCart(@PathVariable Long id) {
        cartService.removeCart(id);
        return Result.ok();
    }

    /**
     * 批量删除选中商品
     */
    @PostMapping("/batchRemove")
    public Result<Void> batchRemove(@RequestBody List<Long> ids) {
        cartService.batchRemove(ids);
        return Result.ok();
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("/clear")
    public Result<Void> clearCart() {
        cartService.clearCart(getUserId());
        return Result.ok();
    }

    /**
     * 获取当前用户购物车中指定商品的数量
     * GET /api/cart/count?goodsId=1
     */
    @GetMapping("/count")
    public Result<Integer> getGoodsCount(@RequestParam Long goodsId) {
        int count = cartService.getGoodsCount(getUserId(), goodsId);
        return Result.ok(count);
    }
}
