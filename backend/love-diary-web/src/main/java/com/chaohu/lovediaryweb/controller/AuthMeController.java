package com.chaohu.lovediaryweb.controller;

import com.chaohu.lovediarycommon.result.Result;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthMeController {
    /**
     * 验证拦截是否生效：
     *   不带 token → 401
     *   带正确 token → 返回用户名
     */
    @GetMapping("/auth/me")
    public Result<String> me() {
        Subject subject = SecurityUtils.getSubject();
        // getPrincipal() 拿到的就是 JwtRealm 里塞进去的 username
        return Result.success((String) subject.getPrincipal());
    }
}
