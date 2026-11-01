package com.chaohu.lovediaryrepository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import javax.management.Notification;
@Mapper

/**
 * 通知表 Mapper，同样靠 BaseMapper 自动获得 CRUD。
 */
public interface NotificationMapper extends BaseMapper<Notification> {
}
