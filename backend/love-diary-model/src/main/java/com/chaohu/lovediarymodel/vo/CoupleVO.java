package com.chaohu.lovediarymodel.vo;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 关系视图对象，前端 CoupleVO 接口与之对齐。
 */
@Data
public class CoupleVO {

    private Long id;
    private Long userAId;                                    // 邀请方
    private Long userBId;                                    // 申请方
    private Integer status;                                  // 0待确认 1已绑定 2已解除
    private LocalDateTime bindTime;                          // 正式绑定时间
    private LocalDate anniversary;                           // 在一起纪念日
    private String nicknameA;                                // 邀请方昵称
    private String nicknameB;                                // 申请方昵称
}
