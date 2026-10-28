package com.chaohu.lovediaryservice.impl;

import com.chaohu.lovediarycommon.context.UserContext;
import com.chaohu.lovediarymodel.entity.User;
import com.chaohu.lovediaryrepository.mapper.UserMapper;
import com.chaohu.lovediaryservice.CoupleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

@Service
public class CoupleServiceImpl implements CoupleService {
    @Resource
    private UserMapper userMapper;
    
    private User me() {
        Long uid = UserContext.getUserId();   // ← 来自 common 模块
        return userMapper.selectById(uid);    // ← 注入进本类的 UserMapper（repository 模块）
    }
}
