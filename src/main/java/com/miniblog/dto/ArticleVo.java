package com.miniblog.dto;

import lombok.Data;
import java.util.List;

@Data
public class ArticleVo {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String coverImage;
    private Long categoryId;
    private String categoryName;
    private String tags;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private Long userId;
    private String authorNickname;
    private String authorAvatar;
    private String createTime;
    private Boolean isLiked;
    private Boolean isFavorited;
}
