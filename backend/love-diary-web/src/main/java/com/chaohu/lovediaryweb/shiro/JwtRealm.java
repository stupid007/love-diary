package com.chaohu.lovediaryweb.shiro;

import com.chaohu.lovediarycommon.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.util.StringUtils;

/**
 * Realm 是 Shiro 与「你的数据」之间的桥梁。
 *
 * 用户名密码登录时，Realm 负责查库比对密码；
 * JWT 登录时，Realm 负责【验签】—— 这就是本类的核心职责。
 *
 * ⚠️ 这里【不要】去查数据库。JWT 是自包含的，验签通过就代表身份可信。
 *    查库是「登录时」做的事，不是「每次请求」做的事 —— 这正是 JWT 无状态的价值。
 */
public class JwtRealm extends AuthorizingRealm {
    private JWTUtil jwtUtil;
    public JwtRealm(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }
    /**
     * 声明本 Realm 只处理 JwtToken。
     * 不重写这个方法的话，Shiro 拿任何 Token 都会来找它，会出问题。
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }
    /** 授权（你能干什么）：S3 先不做，返回 null 即可 */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }
    // 认证（你是谁）；jwt的验签就是在这里
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwt = (String) token.getCredentials();
        if(!StringUtils.hasText(jwt)) {
            throw new AuthenticationException("token为空");
        }
        try {
            // parseToken内部会验签名+过期时间，失败抛出jwtException
            String username = jwtUtil.getUserName(jwt);
            //这里返回的凭证是token本身，shiro不会再拿他取比对密码
            return new SimpleAuthenticationInfo(username, jwt, getName());
        } catch (Exception e) {
            throw new AuthenticationException("token无效或已过期",e);
        }
    }
}
