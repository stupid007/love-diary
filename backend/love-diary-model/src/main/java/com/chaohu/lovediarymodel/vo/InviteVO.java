package com.chaohu.lovediarymodel.vo;

import lombok.Data;

/**
 * 生成邀请码的返回：只回一个码。
 */
@Data
public class InviteVO {

    private String code;                                     // 6 位邀请码
}
