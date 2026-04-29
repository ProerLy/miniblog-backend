package com.miniblog.dto;

import lombok.Data;

@Data
public class AuthVo {
    private String token;
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
}
