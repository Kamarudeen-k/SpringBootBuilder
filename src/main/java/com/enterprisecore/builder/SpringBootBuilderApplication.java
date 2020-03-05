package com.enterprisecore.builder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
		"com.enterprisecore.builder", 
		"com.enterprisecore.configuration",
		"com.enterprisecore.services"})
public class SpringBootBuilderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootBuilderApplication.class, args);
	}

}
