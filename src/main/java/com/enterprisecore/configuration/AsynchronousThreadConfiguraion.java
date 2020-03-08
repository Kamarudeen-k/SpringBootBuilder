package com.enterprisecore.configuration;

import java.util.concurrent.Executor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsynchronousThreadConfiguraion {
	
	@Bean
	public Executor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setCorePoolSize(7);
		taskExecutor.setMaxPoolSize(10);
		taskExecutor.setQueueCapacity(500);
		taskExecutor.setThreadNamePrefix("ConigProcessor: ");
		taskExecutor.initialize();
		return taskExecutor;
	}
}
