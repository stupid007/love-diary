package com.chaohu.lovediarycommon.exception;

import com.chaohu.lovediarycommon.result.ResultCode;
import lombok.Getter;

@Getter
public class BizException extends RuntimeException {
    private final int code;
    public BizException( String message) {
        super(message);
        this.code = ResultCode.FAIL.getCode();
    }
    public BizException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }
}
