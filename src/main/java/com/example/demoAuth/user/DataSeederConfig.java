package com.example.demoAuth.user;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeederConfig {

	@Bean
	CommandLineRunner seedDemoUsers(DemoUserService demoUserService, AppUserRepository appUserRepository) {
		return args -> {
			if (appUserRepository.findByUsername("user").isEmpty()) {
				demoUserService.createDemoUser("user", "password", Set.of("ROLE_USER"));
			}
		};
	}
}
