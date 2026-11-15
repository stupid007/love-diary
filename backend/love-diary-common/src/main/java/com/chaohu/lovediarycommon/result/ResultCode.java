package com.chaohu.lovediarycommon.result;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200,"success"),
    FAIL(500,"系统异常"),
    UNAUTHORIZED(403,"无权限"),
    PARAM_ERROR(400,"参数错误"),
    USERNAME_EXIST(1001,"用户已存在"),
    USER_NOT_FOUND(1002,"用户不存在"),
    PASSWORD_ERROR(1003,"用户名或密码错误"),
    // 情侣关系模块
    // ===== 情侣关系模块 1004~1011 =====
    COUPLE_EXIST(1004, "您已存在情侣关系"),                 // 已绑定或待确认时再操作
    INVITE_CODE_INVALID(1005, "邀请码无效或不存在"),         // B 提交的码查不到
    INVITE_CODE_USED(1006, "该邀请码已被使用"),              // 对方已绑定
    CANNOT_BIND_SELF(1007, "不能与自己缔结羁绊"),            // A、B 是同一人
    COUPLE_NOT_FOUND(1008, "未找到情侣关系"),                // 解绑/资料时查不到
    NOT_INVITER(1009, "只有邀请方可以确认"),                // 非 userA 调用 confirm
    COUPLE_CONFIRMED(1010, "该关系已确认，不可重复操作"),     // confirm 时状态非待确认
    EVER_BOUND_GONE(1011, "你的机会已经没有了，你没有好好珍惜那个她（他）"); // 一生一次用尽



    private final int code;
    private final String message;

    ResultCode(int code,String message){
        this.code = code;
        this.message = message;
    }
}
