package com.chaohu.lovediaryweb.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 把 HTTP 请求里的 JWT 包装成 Shiro 认识的凭证。
 * <p>
 * AuthenticationToken 是 Shiro 对「认证凭证」的抽象：
 * 用户名密码登录时它是 UsernamePasswordToken，
 * 我们这里是 JWT，所以自定义一个。
 */
public class JwtToken implements AuthenticationToken {
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    // 认证通过前主体就是token，告诉shiro我是谁
    @Override
    public Object getPrincipal() {
        return token;
    }

    // 凭证也是token（JWT模式下主体和凭证是同一个东西），相当于“密码”
    @Override
    public Object getCredentials() {
        return token;
    }
}
