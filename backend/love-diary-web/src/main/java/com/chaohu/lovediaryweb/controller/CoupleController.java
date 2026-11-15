package com.chaohu.lovediaryweb.controller;

import com.chaohu.lovediarycommon.result.Result;
import com.chaohu.lovediarymodel.dto.BindDTO;
import com.chaohu.lovediarymodel.dto.CoupleProfileDTO;
import com.chaohu.lovediarymodel.vo.CoupleStateVO;
import com.chaohu.lovediarymodel.vo.InviteVO;
import com.chaohu.lovediaryservice.CoupleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

// 标记为 REST 控制器，返回值自动序列化为 JSON
@RestController
// 路由前缀 /couple；配合前端 baseURL=/api，实际访问路径为 /api/couple/...
@RequestMapping("/couple")
// 用 final 字段生成构造器，由 Spring 完成依赖注入（构造器注入）
@RequiredArgsConstructor
// 开启方法级参数校验，配合 @Valid 使用
@Validated
public class CoupleController {

    // 注入情侣关系业务服务
    private final CoupleService coupleService;

    // 邀请方生成邀请码：POST /couple/invite
    @PostMapping("/invite")
    public Result<InviteVO> invite() {
        // 调用业务层生成邀请码，并直接包装为成功结果返回
        return Result.success(coupleService.invite());
    }

    // 申请方提交邀请码绑定：POST /couple/bind
    @PostMapping("/bind")
    public Result<Void> bind(@Valid @RequestBody BindDTO dto) {
        // @Valid 触发对 BindDTO 字段的校验（邀请码非空、长度 6~16）
        coupleService.bind(dto);
        // 无业务数据返回，仅表示操作成功
        return Result.success();
    }

    // 邀请方确认绑定：POST /couple/confirm?coupleId=xx
    @PostMapping("/confirm")
    public Result<Void> confirm(@RequestParam Long coupleId) {
        // 从请求参数取关系 ID，调用业务层完成确认
        coupleService.confirm(coupleId);
        // 返回成功
        return Result.success();
    }

    // 查询关系四态：GET /couple/state（前端 getCouple 调用这个）
    @GetMapping("/state")
    public Result<CoupleStateVO> state() {
        // 返回 everBound + 当前关系，供前端切换四态视图
        return Result.success(coupleService.state());
    }

    // 解绑：POST /couple/unbind
    @PostMapping("/unbind")
    public Result<Void> unbind() {
        // 调用业务层解除关系（everBound 保留，couple_id 清空）
        coupleService.unbind();
        // 返回成功
        return Result.success();
    }

    // 情侣资料：GET /couple/profile
    @GetMapping("/profile")
    public Result<CoupleProfileDTO> profile() {
        // 返回纪念日与双方昵称快照
        return Result.success(coupleService.profile());
    }
}
