package com.chaohu.lovediarymodel.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 情侣资料:情侣纪念日，双方昵称
 */
@Data
public class CoupleProfileDTO {
    // 恋爱纪念日
    private LocalDateTime anniversary;
    // 邀请方昵称快照
    private String nicknameA;
    // 申请方昵称快照
    private String nicknameB;
}
