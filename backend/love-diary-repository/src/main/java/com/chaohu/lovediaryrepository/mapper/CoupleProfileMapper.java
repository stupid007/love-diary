package com.chaohu.lovediaryrepository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaohu.lovediarymodel.entity.CoupleProfile;
import org.apache.ibatis.annotations.Mapper;

// 情侣资料 Mapper，继承 BaseMapper 获得 CRUD
@Mapper
public interface CoupleProfileMapper extends BaseMapper<CoupleProfile> {
}
