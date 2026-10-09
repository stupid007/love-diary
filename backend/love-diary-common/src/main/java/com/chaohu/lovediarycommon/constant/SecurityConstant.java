package com.chaohu.lovediarycommon.constant;
//JWT
public class SecurityConstant {
    //Token请求头
    public static final String TOKEN_HEADER = "Authorization";

    // Token前缀
    public static final String TOKEN_PREFIX = "Bearer ";

    // Token有效期7天，单位毫秒
    public static final long TOKEN_EXPIRE = 7*24*60*60*1000L;

    //JWT签发者
    public static final String TOKEN_ISSUER = "love-diary";

    //JWT载荷中的用户名的key
    public static final String CLAIM_USERNAME = "username";
}
