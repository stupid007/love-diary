package com.chaohu.lovediaryrepository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaohu.lovediarymodel.entity.Notification;
import org.apache.ibatis.annotations.Mapper;


/**
 * 通知表 Mapper，同样靠 BaseMapper 自动获得 CRUD。
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
}
