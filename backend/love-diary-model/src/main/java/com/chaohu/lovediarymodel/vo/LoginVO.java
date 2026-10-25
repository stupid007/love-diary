package com.chaohu.lovediarymodel.vo;

import lombok.Data;

@Data
public class LoginVO {
    private String token;
    private Long userId;
    private String nickName;
}
