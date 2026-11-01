package com.chaohu.lovediarymodel.vo;

import lombok.Data;

/**
 * 关系状态：前端据此切四态视图。
 * everBound=true 且 couple=null → 显示"机会已尽"哲学卡片。
 */
@Data
public class CoupleStateVO {

    private Boolean everBound;                               // 是否曾绑定（一生一次）
    private CoupleVO couple;                                 // 当前关系，无则 null
}
