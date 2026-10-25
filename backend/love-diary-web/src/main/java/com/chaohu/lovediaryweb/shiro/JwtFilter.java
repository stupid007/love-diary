package com.chaohu.lovediaryweb.shiro;

import com.chaohu.lovediarycommon.constant.SecurityConstant;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;

/**
 * JWT 拦截器。
 * <p>
 * 它只做两件事：① 从请求头取 token ② 触发 Subject.login() 交给 Realm 验签。
 * 验签逻辑【不在】这里，在 JwtRealm 里 —— 这是 Shiro 的设计分工。
 */
public class JwtFilter extends AuthenticatingFilter {
    /**
     * 从请求里取出 token 包装成 JwtToken。取不到返回 null
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        String jwt = resolveToken((HttpServletRequest) servletRequest);
        return StringUtils.hasText(jwt) ? new JwtToken(jwt) : null;
    }

    // 返回ture放行，false进入onAccessDenied, isAccessAllowed：判断是否直接放行
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) {
        // 跨域预检请求直接放行，否则前端所有跨域请求都会失败
        if (RequestMethod.OPTIONS.equals(((HttpServletRequest) servletRequest).getMethod())) {
            return true;
        }
        // 无状态模式下每次请求都没有会话，所以一律返回false
        // 交给 onAccessDenied去执行认证
        return false;
    }

    //onAccessDenied：处理被拒绝的访问
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        // 压根没带 token：直接 401，不必走认证流程
        if (createToken(servletRequest, servletResponse) == null) {
            writeUnauthorized(servletResponse, "未登录或 token 缺失");
            return false;
        }
        // 带了 token：交给 Realm 验签
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     * Realm 抛 AuthenticationException 时会走到这里
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e,
                                     ServletRequest request, ServletResponse response) {
        writeUnauthorized(response, "token 无效或已过期");
        return false;
    }

    /**
     * 从 Authorization 头里取 token，剥掉 "Bearer " 前缀
     */
    private String resolveToken(HttpServletRequest request) {
        String header = request.getHeader(SecurityConstant.TOKEN_HEADER);   // "Authorization"
        if (!StringUtils.hasText(header)) {
            return null;
        }
        if (header.startsWith(SecurityConstant.TOKEN_PREFIX)) {             // "Bearer "
            return header.substring(SecurityConstant.TOKEN_PREFIX.length()).trim();
        }
        return header.trim();
    }

    /**
     * 统一输出 401 + JSON，而不是 Shiro 默认的跳转登录页
     */
    private void writeUnauthorized(ServletResponse response, String message) {
        HttpServletResponse resp = (HttpServletResponse) response;
        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
        resp.setContentType("application/json;charset=UTF-8");
        try {
            resp.getWriter().write("{\"code\":403,\"message\":\"" + message + "\",\"data\":null}");
        } catch (IOException ignored) {
        }
    }
}
