package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @org.springframework.beans.factory.annotation.Value("${kindr.upload-dir:uploads}")
    private String uploadDirectory = "uploads";
	@Override
	public void addCorsMappings(CorsRegistry r) {
		r.addMapping("/api/**")
				.allowedOrigins("http://localhost:3000", "http://127.0.0.1:3000", "http://localhost:8080",
						"http://127.0.0.1:8080", "http://localhost:5173", "http://127.0.0.1:5173",
						"http://localhost:4173", "http://127.0.0.1:4173")
				.allowedMethods("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS").allowedHeaders("*")
				.allowCredentials(true);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Serve uploaded files from the uploads/ folder at /uploads/**
		String location = java.nio.file.Path.of(uploadDirectory).toAbsolutePath().normalize().toUri().toString();
		registry.addResourceHandler("/uploads/**").addResourceLocations(location.endsWith("/") ? location : location + "/");
	}
}
