package com.enterprisecore.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.enterprisecore.configuration.ConfigurationProcessor;
import com.enterprisecore.model.APIApplication;

@Service
public class SpringBootAppGeneratorService {
	
	@Autowired
	ConfigurationProcessor configProcessor;
	private static final Logger LOG = LoggerFactory.getLogger(SpringBootAppGeneratorService.class);
	
	public SpringBootAppGeneratorService() {
		
	}
	
	//@Async
	public boolean generateSpringBootApp(APIApplication apiApp) {
		LOG.info("App generator service started for organisation: "+apiApp.getOrganisationName());
	
		return configProcessor.generateSpringBootApps(apiApp);
		
	}
}
