package com.chaohu.lovediaryservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.spring.service.impl.ServiceImpl;
import com.chaohu.lovediarymodel.entity.Notification;
import com.chaohu.lovediarymodel.vo.NotificationVO;
import com.chaohu.lovediarymodel.vo.NotifyListVO;
import com.chaohu.lovediaryrepository.mapper.NotificationMapper;
import com.chaohu.lovediaryservice.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

// 标记为 Spring 业务 Bean，交给容器管理
@Service
// 用 final 字段生成构造器，由 Spring 完成依赖注入（构造器注入）
@RequiredArgsConstructor
// 继承 MP 的 ServiceImpl 获得 baseMapper 及通用 CRUD，并实现自定义通知接口
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements NotificationService {

    // 通知类型常量：申请绑定
    private static final int TYPE_APPLY = 1;
    // 通知类型常量：确认绑定
    private static final int TYPE_CONFIRM = 2;
    // 通知类型常量：解除绑定
    private static final int TYPE_DISSOLVE = 3;

    // 注入通知表 Mapper（也可直接用父类的 this.baseMapper）
    private final NotificationMapper notificationMapper;

    // 发送一条通知的实现
    @Override
    public void notify(Long userId, String type, String title, String content, Long refId) {
        // 新建一条通知实体
        Notification n = new Notification();
        // 设置收件人用户 ID
        n.setUserId(userId);
        // 把字符串类型名转成整数再存库，便于枚举与统计
        n.setType(typeCode(type));
        // 设置卡片标题
        n.setTitle(title);
        // 设置卡片正文
        n.setContent(content);
        // 设置关联对象 ID（这里存的是 coupleId）
        n.setRefId(refId);
        // 初始为未读;
        n.setIsRead(0);
        // 插入数据库
        notificationMapper.insert(n);
    }

    // 查询某用户的通知列表与未读数
    @Override
    public NotifyListVO list(Long userId) {
        // 按用户 ID 查全部通知，并按创建时间倒序
        List<Notification> raw = notificationMapper.selectList(
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getUserId, userId)
                        .orderByDesc(Notification::getCreateTime));
        // 把实体列表逐条转换成前端使用的 VO
        List<NotificationVO> vos = raw.stream().map(n -> {
            // 新建单条 VO
            NotificationVO v = new NotificationVO();
            // 复制主键
            v.setId(n.getId());
            // 把整数类型还原成字符串类型名，方便前端识别
            v.setType(typeName(n.getType()));
            // 复制标题
            v.setTitle(n.getTitle());
            // 复制正文
            v.setContent(n.getContent());
            // 复制关联 ID
            v.setRefId(n.getRefId());
            // 把整数 isRead 转成 Boolean（1 表示已读）
            v.setIsRead(n.getIsRead() != null && n.getIsRead() == 1);
            // 复制创建时间
            v.setCreateTime(n.getCreateTime());
            // 返回该 VO 参与收集
            return v;
        }).collect(Collectors.toList());
        // 统计未读条数（isRead 为 null 或 0 都算未读）
        long unread = raw.stream().filter(n -> n.getIsRead() == null || n.getIsRead() == 0).count();
        // 新建列表包装对象
        NotifyListVO vo = new NotifyListVO();
        // 设置通知列表
        vo.setList(vos);
        // 设置未读数（强转 int）
        vo.setUnread((int) unread);
        // 返回最终结果
        return vo;
    }

    // 标记单条通知为已读
    @Override
    public void read(Long id) {
        // 先按主键查出这条通知
        Notification n = notificationMapper.selectById(id);
        // 查不到直接返回，避免空指针
        if (n == null) return;
        // 置为已读
        n.setIsRead(1);
        // 更新到数据库
        notificationMapper.updateById(n);
    }

    // 把某用户的通知全部标记为已读
    @Override
    public void readAll(Long userId) {
        // 构造更新实体，仅设置已读字段
        Notification u = new Notification();
        // 置为已读
        u.setIsRead(1);
        // 批量更新：仅把该用户且未读的记录置为已读
        notificationMapper.update(u,
                new LambdaUpdateWrapper<Notification>()
                        .eq(Notification::getUserId, userId)
                        .eq(Notification::getIsRead, 0));
    }

    // 把类型名转换成存储用的整数
    private int typeCode(String name) {
        // 根据字符串匹配返回对应整数
        switch (name) {
            // 申请绑定类型
            case "COUPLE_APPLY": return TYPE_APPLY;
            // 确认绑定类型
            case "COUPLE_CONFIRM": return TYPE_CONFIRM;
            // 解除绑定类型
            case "COUPLE_DISSOLVE": return TYPE_DISSOLVE;
            // 未知类型默认 0
            default: return 0;
        }
    }

    // 把存储用的整数还原成类型名
    private String typeName(int code) {
        // 根据整数匹配返回对应字符串
        switch (code) {
            // 申请绑定类型
            case TYPE_APPLY: return "COUPLE_APPLY";
            // 确认绑定类型
            case TYPE_CONFIRM: return "COUPLE_CONFIRM";
            // 解除绑定类型
            case TYPE_DISSOLVE: return "COUPLE_DISSOLVE";
            // 未知类型
            default: return "UNKNOWN";
        }
    }
}
