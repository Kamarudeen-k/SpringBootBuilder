package com.enterprisecore.services;

import java.util.Optional;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;

import com.enterprisecore.configuration.ConfigurationProcessor;
import com.enterprisecore.model.SpringBootConfigurator;

public class SpringBootConfiguratorService {
	
	@Autowired
	ConfigurationProcessor configProcessor;
	private static final Logger LOG = LoggerFactory.getLogger(SpringBootConfiguratorService.class);
	
	public SpringBootConfiguratorService() {
		
	}
	
	@Async
	public Future<Optional<?>> configureSpringBootApp(SpringBootConfigurator configs) {
		LOG.info("SpringBootAppConfigurator service started for: "+configs.getApiType() + configs.getApiEndpoint());
	
		return new AsyncResult<>(configProcessor.configureSpringBootAPI(configs));
		
	}

}
