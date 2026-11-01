package com.chaohu.lovediarymodel.vo;

import lombok.Data;
import java.time.LocalDateTime;

/**
 * 单条通知视图，前端 NotificationVO 接口与之对齐。
 * 注意 isRead 在实体里是 Integer(0/1)，这里转成 Boolean 给前端。
 */
@Data
public class NotificationVO {

    private Long id;
    private String type;                                     // COUPLE_APPLY / CONFIRM / DISSOLVE
    private String title;                                    // 卡片标题
    private String content;                                  // 卡片正文
    private Long refId;                                      // 关联 couple_id
    private Boolean isRead;                                  // 是否已读（Boolean）
    private LocalDateTime createTime;                        // 创建时间
}
