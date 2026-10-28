package com.chaohu.lovediarymodel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("couple")
public class Couple {
    @TableId(type = IdType.AUTO)
    private Long id;
    // 邀请人ID
    private Long userAId;
    // 申请方ID
    private Long userBId;
    // 状态
    private Integer status;
    // 正式绑定时间
    private LocalDateTime bindTime;
    // 在anniversary;一起纪念日
    private LocalDate anniversary;
    // 邀请方昵称快照
    private String nicknameA;
    // 申请方昵称快照
    private String nicknameB;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;
    // 逻辑删除标志
    @TableLogic
    private Integer deleted;
}
