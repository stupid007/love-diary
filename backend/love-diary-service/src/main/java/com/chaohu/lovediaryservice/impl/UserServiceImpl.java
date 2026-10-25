package com.chaohu.lovediaryservice.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.chaohu.lovediarycommon.exception.BizException;
import com.chaohu.lovediarycommon.result.ResultCode;
import com.chaohu.lovediarycommon.util.JWTUtil;
import com.chaohu.lovediarymodel.dto.LoginDTO;
import com.chaohu.lovediarymodel.dto.RegisterDTO;
import com.chaohu.lovediarymodel.entity.User;
import com.chaohu.lovediarymodel.vo.LoginVO;
import com.chaohu.lovediaryrepository.mapper.UserMapper;
import com.chaohu.lovediaryservice.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final JWTUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterDTO user) {
        //1.查重
        Long count = userMapper.selectCount(new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername()));
        if (count != null && count > 0) {
            throw new BizException(ResultCode.USERNAME_EXIST);
        }
        //2.加密
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setNickName(StringUtils.hasText(user.getNickName()) ? user.getNickName() : user.getUsername());

        //3.入库
        userMapper.insert(newUser);
    }

    @Override
    public LoginVO login(LoginDTO user) {
        //1.按照用户名查（delete=0是由@TableLogic自动追加）
        User userExist = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, user.getUsername())
        );
        if (userExist == null) {
            throw new BizException(ResultCode.USER_NOT_FOUND);
        }

        //2、比对密码
        if (!passwordEncoder.matches(user.getPassword(), userExist.getPassword())) {
            throw new BizException(ResultCode.PASSWORD_ERROR);
        }
        //3、签发token
        String token = jwtUtil.generateToken(userExist.getId(), user.getUsername());
        // 封装返回
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(userExist.getId());
        vo.setNickName(userExist.getNickName());
        return vo;
    }
}
