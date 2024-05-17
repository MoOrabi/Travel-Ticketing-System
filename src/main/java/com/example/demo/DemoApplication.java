package com.example.demo;

import com.example.demo.config.SecurityConfig;
import com.example.demo.constants.AppUserRole;
import com.example.demo.entity.AppUser;
import com.example.demo.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Profile({"prod"})
	@Bean
	CommandLineRunner run(AppUserRepository appUserRepository) {
		return args -> {

			appUserRepository.deleteAll();
			AppUser appUser = new AppUser("Mo", "Mo", "h1@g.c", SecurityConfig.passwordEncoder().encode("12345678"), AppUserRole.ROLE_ADMIN, false, true);

			appUserRepository.save(appUser);
		};
	}
}
