package com.chaohu.lovediaryweb;

import com.chaohu.lovediarymodel.entity.User;
import com.chaohu.lovediaryrepository.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;
@SpringBootTest
public class userMapperTest {
    @Autowired
    UserMapper userMapper;
    @Test
    public void testUserMapper() {
        User user = new User();
        user.setUsername("test_user");
        user.setPassword("$2a$10$fakehashfakehashfakehashfakehashfakehashfakehashfa");
        user.setNickName("测试用户");

        userMapper.insert(user);
        //主键会自动回填
        assertNotNull(user.getId());
        System.out.println("插入成功,id = "+user.getId());

        User found = userMapper.selectById(user.getId());
        System.out.println("查询返回："+found);
    }
}
