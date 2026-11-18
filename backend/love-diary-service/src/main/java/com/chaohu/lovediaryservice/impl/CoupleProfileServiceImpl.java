package com.chaohu.lovediaryservice.impl;

import com.chaohu.lovediarymodel.dto.CoupleProfileDTO;
import com.chaohu.lovediarymodel.dto.CoupleProfileUpdateDTO;
import com.chaohu.lovediarymodel.vo.MapPointVO;
import com.chaohu.lovediaryrepository.mapper.CoupleMapper;
import com.chaohu.lovediaryrepository.mapper.CoupleProfileMapper;
import com.chaohu.lovediaryrepository.mapper.UserMapper;
import com.chaohu.lovediaryservice.CoupleProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoupleProfileServiceImpl implements CoupleProfileService {
    //关系状态 已绑定（1）
    private static  final int STATUS_ACTIVE = 1;
    // A 点位颜色：青蓝
    private static final String COLOR_A = "#38f0ff";
    // B 点位颜色：粉珊瑚
    private static final String COLOR_B = "#ff5fa2";
    // 关系 Mapper
    private final CoupleMapper coupleMapper;
    // 用户mapper
    private final UserMapper userMapper;
    // 资料mapper
    private final CoupleProfileMapper coupleProfileMapper;
    @Override
    public CoupleProfileDTO profile() {
        return null;
    }

    @Override
    public void save(CoupleProfileUpdateDTO dto) {

    }

    @Override
    public List<MapPointVO> mapPoints() {
        return List.of();
    }
}
