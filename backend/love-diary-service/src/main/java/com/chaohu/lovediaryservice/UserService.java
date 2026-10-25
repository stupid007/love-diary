package com.chaohu.lovediaryservice;

import com.chaohu.lovediarymodel.dto.LoginDTO;
import com.chaohu.lovediarymodel.dto.RegisterDTO;
import com.chaohu.lovediarymodel.vo.LoginVO;

public interface UserService {
    /** 注册：查重 → 加密 → 落库 */
    void register(RegisterDTO user);
    /** 登录：查用户 → 比对密码 → 签发 token */
    LoginVO login(LoginDTO user);
}
