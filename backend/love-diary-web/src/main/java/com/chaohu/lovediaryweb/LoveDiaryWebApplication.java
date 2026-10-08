package com.chaohu.lovediaryweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.chaohu")
public class LoveDiaryWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoveDiaryWebApplication.class, args);
	}

}
