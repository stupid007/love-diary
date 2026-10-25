package com.chaohu.lovediaryrepository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.chaohu.lovediarymodel.entity.User;
import org.apache.ibatis.annotations.Mapper;
/**
 * 继承 BaseMapper<User> 就自动拥有：
 *   insert / deleteById / updateById / selectById / selectList / selectCount / selectOne ...
 * 不需要自己写一行 SQL —— 这是 MyBatis-Plus 的核心价值。
 * 后面遇到复杂查询（多表联查、聚合）时，再手写 @Select 或 XML。
 */
@Mapper                       // 标记为 MyBatis 的 Mapper 接口（配合启动类的 @MapperScan）
public interface UserMapper extends BaseMapper<User> {
}
