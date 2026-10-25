package com.chaohu.lovediaryweb.config;

import com.chaohu.lovediarycommon.util.JWTUtil;
import com.chaohu.lovediaryweb.shiro.JwtFilter;
import com.chaohu.lovediaryweb.shiro.JwtRealm;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShiroConfig {

    @Bean
    public JwtRealm jwtRealm(JWTUtil jwtUtil) {
        return new JwtRealm(jwtUtil);
    }

    /**
     * 安全管理器。
     *
     * ⚠️ 返回类型用 DefaultWebSecurityManager 而不是 SecurityManager：
     *    java.lang.SecurityManager（JDK 的，Java 17 已废弃）和
     *    org.apache.shiro.mgt.SecurityManager 同名，裸写会被 java.lang 截胡。
     */
    @Bean
    public DefaultWebSecurityManager securityManager(JwtRealm jwtRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(jwtRealm);
        // 【关键】关掉 Shiro 自带的 Session —— JWT 是无状态的，不需要服务端会话。
        // 不关的话 Shiro 会给每个请求建 Session，无状态架构就名存实亡了。
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(evaluator);
        manager.setSubjectDAO(subjectDAO);

        return manager;
    }

    /** bean 名 "jwt" 就是下面过滤链里引用的名字 */
    @Bean(name = "jwt")
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    /**
     * 【极易踩的坑】必须阻止 Spring Boot 把 JwtFilter 自动注册成全局 Servlet Filter。
     *
     * 原因：JwtFilter 是 jakarta.servlet.Filter 的实现类，
     *      Spring Boot 会把容器里所有 Filter 类型的 Bean 自动注册到【所有】URL 上。
     *      这样它会绕过 Shiro 的链规则，连本该放行的 /auth/login 也被拦掉。
     *
     * setEnabled(false) = 告诉 Spring Boot：这个 Filter 只归 Shiro 管，你别插手。
     */
    @Bean
    public FilterRegistrationBean<JwtFilter> jwtFilterRegistration(JwtFilter jwtFilter) {
        FilterRegistrationBean<JwtFilter> registration = new FilterRegistrationBean<>(jwtFilter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * URL 拦截规则：从上到下匹配，第一个命中的生效。
     * 所以 /** 必须放最后，否则后面的规则永远匹配不到。
     */
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        definition.addPathDefinition("/auth/register", "anon");   // 注册免登录
        definition.addPathDefinition("/auth/login", "anon");      // 登录免登录
        definition.addPathDefinition("/hello", "anon");           // 保留的测试接口
        definition.addPathDefinition("/**", "jwt");               // 其余全部需要 token
        return definition;
    }
}
