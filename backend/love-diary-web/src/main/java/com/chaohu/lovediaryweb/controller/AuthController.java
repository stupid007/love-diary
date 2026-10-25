package com.chaohu.lovediaryweb.controller;

import com.chaohu.lovediarycommon.result.Result;
import com.chaohu.lovediarymodel.dto.LoginDTO;
import com.chaohu.lovediarymodel.dto.RegisterDTO;
import com.chaohu.lovediarymodel.vo.LoginVO;
import com.chaohu.lovediaryservice.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    public Result<Void> register(@Valid  @RequestBody RegisterDTO user) {
        userService.register(user);
        return Result.success();
    }
    @PostMapping("/login")
    public Result<LoginVO> login(@Valid @RequestBody LoginDTO user) {
        LoginVO login = userService.login(user);
        return Result.success(login);
    }
}
