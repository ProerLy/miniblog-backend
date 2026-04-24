package com.mall.entity;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class PageResult<T> implements Serializable {
    private Long total;
    private List<T> list;

    public static <T> PageResult<T> of(Long total, List<T> list) {
        PageResult<T> r = new PageResult<>();
        r.setTotal(total);
        r.setList(list);
        return r;
    }
}
