package com.capstone.lifesabit.gateguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
@SpringBootApplication
public class GateGuardApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(GateGuardApplication.class, args);
	}

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS").allowedOrigins("https://gate-guard.com", "https://www.gate-guard.com", "http://localhost:3000").allowCredentials(true);
    }
}
