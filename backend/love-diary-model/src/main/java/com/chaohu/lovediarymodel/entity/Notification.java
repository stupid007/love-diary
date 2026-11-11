package com.chaohu.lovediarymodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Natification {
    @TableId(type = IdType.AUTO)
    private Long id;
    // 收件人ID
    private Long userId;
    // 类型
    private Integer type;
    // 卡片标题
    private String title;
    // 卡片内容
    private String content;
    // 关联对象
    private Long refId;
    // 是否已读
    private Integer isRedad;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
}
