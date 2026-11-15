package com.chaohu.lovediaryweb.controller;

import com.chaohu.lovediarycommon.context.UserContext;
import com.chaohu.lovediarycommon.result.Result;
import com.chaohu.lovediarymodel.vo.NotifyListVO;
import com.chaohu.lovediaryservice.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

// 标记为 REST 控制器，返回值自动序列化为 JSON
@RestController
// 路由前缀 /notify；配合前端 baseURL=/api，实际访问路径为 /api/notify/...
@RequestMapping("/notify")
// 用 final 字段生成构造器，由 Spring 完成依赖注入（构造器注入）
@RequiredArgsConstructor
public class NotificationController {

    // 注入通知业务服务
    private final NotificationService notificationService;

    // 查询通知列表与未读数：GET /notify/list
    @GetMapping("/list")
    public Result<NotifyListVO> list() {
        // 当前登录用户 ID 从 ThreadLocal 取，再调用业务层查询
        return Result.success(notificationService.list(UserContext.getUserId()));
    }

    // 标记单条已读：POST /notify/read?id=xx
    @PostMapping("/read")
    public Result<Void> read(@RequestParam Long id) {
        // 调用业务层把指定通知置为已读
        notificationService.read(id);
        // 返回成功
        return Result.success();
    }

    // 全部已读：POST /notify/read-all
    @PostMapping("/read-all")
    public Result<Void> readAll() {
        // 当前登录用户 ID 从 ThreadLocal 取，把其所有未读通知批量置为已读
        notificationService.readAll(UserContext.getUserId());
        // 返回成功
        return Result.success();
    }
}
