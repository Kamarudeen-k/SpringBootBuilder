package com.enterprisecore.builder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enterprisecore.model.SpringBootConfigurator;
import com.enterprisecore.services.SpringBootConfiguratorService;

@RestController
public class SpringBootConfiguratorController {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpringBootConfiguratorController.class);
	
	@Autowired
	SpringBootConfiguratorService configurator;
	
	@PatchMapping(path="/spring-boot-configurator", consumes="application/json", produces="application/json")
	public CompletableFuture<ResponseEntity<?>> generateSpringBootApp(
			@RequestBody Optional<SpringBootConfigurator> configs){
		ResponseEntity<SpringBootConfigurator> response = null;
		SpringBootConfigurator responseBody = null;
		try {
			Future<SpringBootConfigurator> result = null;
			if(configs.isPresent())
				result = configurator.springBootAppConfigurationProcessor(configs.get());
			
			responseBody = result.get();
			
			if(responseBody != null)
				response = ResponseEntity.ok(responseBody);
			else
				response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
		}catch(Exception ex) {
			LOG.error(ex.getMessage());
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		return CompletableFuture.completedFuture(response);
	}
}
