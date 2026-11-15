package com.chaohu.lovediaryservice;

import com.chaohu.lovediarymodel.vo.NotifyListVO;
// 站内通知业务
public interface NotificationService {
    // 发通知
    void notify(Long userId, String type, String title, String content, Long refId);
    // 查列表 + 未读数
    NotifyListVO list(Long userId);
    // 单条已读
    void read(Long id);
    // 全部已读
    void readAll(Long userId);
}
