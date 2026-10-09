package com.chaohu.lovediarycommon.result;

import lombok.Data;

import java.io.Serializable;

// 封装返回结果
@Data
public class Result<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    public static <T> Result<T> success() {
        return build(ResultCode.SUCCESS, null);
    }

    public static <T> Result<T> success(T data) {
        return build(ResultCode.SUCCESS, data);
    }

    public static <T> Result<T> fail(String message) {
        Result<T> r = new Result<>();
        r.setCode(ResultCode.FAIL.getCode());
        r.setMessage(ResultCode.FAIL.getMessage());
        return r;
    }

    public static <T> Result<T> fail(int code) {
        return build(ResultCode.FAIL, null);
    }

    public static <T> Result<T> build(ResultCode resultCode, T data) {
        Result<T> r = new Result<>();
        r.code = resultCode.getCode();
        r.message = resultCode.getMessage();
        r.data = data;
        return r;
    }
}
