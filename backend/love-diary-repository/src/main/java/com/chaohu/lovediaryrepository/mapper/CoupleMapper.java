package com.chaohu.lovediaryrepository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaohu.lovediarymodel.entity.Couple;
import org.apache.ibatis.annotations.Mapper;
/**
 * 关系表 Mapper。
 * 继承 BaseMapper 即拥有 insert/updateById/selectById/selectOne/selectCount/selectList...
 * 简单 CRUD 不用写一行 SQL；后续复杂联查再补 @Select。
 */
@Mapper
public interface CoupleMapper extends BaseMapper<Couple> {
}
