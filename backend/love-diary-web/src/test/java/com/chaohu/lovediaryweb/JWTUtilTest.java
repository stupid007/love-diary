package com.chaohu.lovediaryweb;

import com.chaohu.lovediarycommon.util.JWTUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JWTUtilTest {

    @Autowired
    private JWTUtil jwtUtil;

    @Test
    void test1() {
        String token = jwtUtil.generateToken(1L, "alice");
        System.out.println("token = " + token);

        assertTrue(jwtUtil.validate(token), "刚签发的 token 应该有效");
        assertEquals("alice", jwtUtil.getUserName(token));
        assertEquals(1L, jwtUtil.getUserId(token));
    }

    @Test
    void test2() {
        String token = jwtUtil.generateToken(1L, "alice");

        // ❌ 错误示范：末尾追加字符
        //    如果某段长度恰好是 4 的倍数，追加 1 个字符只会产生"不足一字节的残余位"，
        //    解码器直接丢弃，解码结果一字不变 → 验签照样通过。
        // assertFalse(jwtUtil.validate(token + "x"));

        // ✅ 正确做法：改掉【中间】某个字符，一定改变解码后的字节
        assertFalse(jwtUtil.validate(flipChar(token, token.indexOf('.') + 5)),
                "篡改 payload 必须校验失败");
        assertFalse(jwtUtil.validate(flipChar(token, token.lastIndexOf('.') + 5)),
                "篡改签名必须校验失败");
    }

    /**
     * 把第 index 个字符换成一个不同的 Base64Url 字符（'A' ↔ 'B'）
     */
    private static String flipChar(String s, int index) {
        char[] cs = s.toCharArray();
        cs[index] = (cs[index] == 'A') ? 'B' : 'A';
        return new String(cs);
    }
}
