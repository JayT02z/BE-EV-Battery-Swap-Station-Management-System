package com.boilerplate.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableScheduling
public class AuthUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthUserApplication.class, args);
		System.out.println("Auth User chạy được rồi nè hẹ hẹ :))");
	}

}
