package com.chaohu.lovediarycommon.exception;

import com.chaohu.lovediarycommon.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //业务异常
    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException e) {
        log.warn("业务异常:{}", e.getMessage());
        return Result.fail(e.getMessage());
    }

    // 统一处理 Spring 参数校验异常，当 @Valid/@Validated 校验失败时会进入此方法
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        // 从异常中获取第一个校验失败的字段错误信息
        FieldError fieldError = e.getBindingResult().getFieldError();
        // 如果拿不到具体字段错误，则返回默认提示；否则返回校验注解中配置的 message
        String msg = fieldError == null ? "参数绑定失败" : fieldError.getDefaultMessage();
        // 封装校验失败信息并返回统一响应结果
        return Result.fail(msg);
    }

    //兜底异常
    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return Result.fail(e.getMessage());
    }
}
