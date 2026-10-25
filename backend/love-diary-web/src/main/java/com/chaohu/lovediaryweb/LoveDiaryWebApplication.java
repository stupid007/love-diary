package com.chaohu.lovediaryweb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.chaohu")
// 关键！不写这个，UserMapper 不会被注册成 Bean
@MapperScan("com.chaohu.lovediaryrepository.mapper")
public class LoveDiaryWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoveDiaryWebApplication.class, args);
	}

}
