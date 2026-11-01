package com.chaohu.lovediarymodel.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 绑定请求体，申请方B提交邀请方A的邀请码
 */
@Data
public class BindDTO {
    @NotBlank(message="邀请码不能为空")
    @Size(min = 6,max = 16,message = "邀请码格式不正确")
    private String code;
}
