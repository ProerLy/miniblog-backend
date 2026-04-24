package com.mall.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("goods")
public class Goods {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String subtitle;
    private BigDecimal price;
    private BigDecimal originalPrice;
    private String mainImage;
    private String images;
    private Long categoryId;
    private Integer stock;
    private Integer sales;
    private String description;
    private String detail;
    private Integer isHot;
    private Integer isNew;
    private Integer isRec;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    @TableLogic
    private Integer deleted;

    /** 前端字段 pics，映射到数据库 images */
    @com.fasterxml.jackson.annotation.JsonProperty("pics")
    public String getPic() {
        return this.mainImage;
    }
}
