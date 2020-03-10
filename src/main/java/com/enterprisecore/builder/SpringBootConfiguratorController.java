package com.enterprisecore.builder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enterprisecore.model.SpringBootConfigurator;

@RestController
public class SpringBootConfiguratorController {
	
	private static final Logger LOG = LoggerFactory.getLogger(SpringBootConfiguratorController.class);
	
	@PatchMapping(path="/spring-boot-configurator", consumes="application/json", produces="application/json")
	public CompletableFuture<ResponseEntity<?>> generateSpringBootApp(
			@RequestBody Optional<SpringBootConfigurator> configs){
		ResponseEntity<?> response = null;
		try {
			Future<Boolean> result = null;
			
			
		}catch(Exception ex) {
			LOG.error(ex.getMessage());
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		return CompletableFuture.completedFuture(response);
	}
}
