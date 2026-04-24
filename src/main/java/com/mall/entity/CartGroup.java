package com.mall.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车店铺分组 DTO
 * 前端需要: [{ shopId, shopName, allSelected, goods: [{id, goodsId, name, pic, skuText, price, num, selected}] }]
 */
@Data
public class CartGroup {
    private Long shopId;
    private String shopName;
    private Boolean allSelected = false;
    private List<CartItem> goods;

    @Data
    public static class CartItem {
        private Long id;
        private Long goodsId;
        private String name;
        private String pic;
        private String skuText; // 规格（前端字段）
        private BigDecimal price;
        private Integer num;
        private Boolean selected = false;
    }
}
