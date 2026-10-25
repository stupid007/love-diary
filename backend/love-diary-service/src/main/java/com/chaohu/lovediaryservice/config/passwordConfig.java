package com.chaohu.lovediaryservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
/**
 * 密码加密器。
 * 做成 Bean 而不是在 Service 里 new，是为了将来能整体换成 Argon2 而不改业务代码。
 */
@Configuration
public class passwordConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
