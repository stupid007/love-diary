package com.chaohu.lovediarycommon.util;

import com.chaohu.lovediarycommon.constant.SecurityConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Date;

import static org.yaml.snakeyaml.tokens.Token.ID.Key;

/**
 * JWT 工具类：只负责「签发」和「解析校验」。
 * <p>
 * 注意它【不参与请求拦截】—— 拦截是 Shiro 的事（S3-④ 才做）。
 * 这个类是一个纯粹的工具，可以被 Service 直接调用。
 */
@Slf4j
@Component
public class JWTUtil {
    /**
     * 从yml文件里面读取密钥，不写死的原因是因为可以在yml里面随时更换
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * 设置token过期时间
     */
    @Value("${jwt.expire}")
    private Long expire;

    /**
     * 把配置里的字符串密钥转成 HMAC-SHA 签名密钥。
     * <p>
     * ⚠️ 头号大坑：Keys.hmacShaKeyFor() 要求密钥 >= 32 字节（256 bit），
     * 否则直接抛 WeakKeyException。中文字符按 UTF-8 算 3 字节，
     * 所以密钥字符串最好用英文数字，长度 >= 32。
     */
    private SecretKey signKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(Long userId, String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuer(SecurityConstant.TOKEN_ISSUER)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expire))
                .signWith(signKey())
                .compact();
    }

    /**
     * 解析并校验 token。
     * <p>
     * 内部会做两件事：① 用密钥重算签名比对（防篡改）② 检查 exp（防过期）。
     * 任何一项不过，都会抛 io.jsonwebtoken.JwtException 的子类。
     * <p>
     * ⚠️ 这是 jjwt 0.12.x 的新 API。网上 0.9 / 0.11 的老写法
     * Jwts.parser().setSigningKey(...).parseClaimsJws(...)
     * 在这个版本编译不过，别抄。
     */

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(signKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 从token里面取用户名和（就是sub）
    public String getUserName(String token) {
        return parseToken(token).getSubject();
    }
    /**
     * 从 token 里取 userId
     */
    public Long getUserId(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Long.class);
    }
    //布尔版校验，给Filter做能不能放行判断用
    public boolean validate(String token) {
        try {
            parseToken(token);
            return true;
        } catch (Exception e) {
            log.warn("token 校验失败:{}",e.getMessage());
            return false;
        }
    }
}
