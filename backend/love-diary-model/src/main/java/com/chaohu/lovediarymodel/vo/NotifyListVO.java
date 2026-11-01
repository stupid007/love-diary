package com.chaohu.lovediarymodel.vo;

import lombok.Data;
import java.util.List;

/**
 * 通知列表 + 未读数，铃铛红点直接用 unread。
 */
@Data
public class NotifyListVO {

    private List<NotificationVO> list;                       // 卡片列表
    private Integer unread;                                  // 未读条数
}
