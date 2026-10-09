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
    PASSWORD_ERROR(1003,"用户名或密码错误");


    private final int code;
    private final String message;

    ResultCode(int code,String message){
        this.code = code;
        this.message = message;
    }
}
