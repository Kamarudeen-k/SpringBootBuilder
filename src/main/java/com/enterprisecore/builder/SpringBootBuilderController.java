package com.enterprisecore.builder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.enterprisecore.model.APIApplication;
import com.enterprisecore.services.SpringBootAppGeneratorService;


@RestController
public class SpringBootBuilderController {

	@Autowired 
	SpringBootAppGeneratorService appGenService; 
	private static final Logger LOG = LoggerFactory.getLogger(SpringBootBuilderController.class);
	
	@PostMapping(path="/spring-boot-app", consumes="application/json")
	public CompletableFuture<ResponseEntity<?>> generateSpringBootApp(
			@RequestBody Optional<APIApplication> apiApp){
		ResponseEntity<?> response = null;
		try {
			Future<Boolean> result = null;
			if(apiApp.isPresent())
				result = appGenService.generateSpringBootApp(apiApp.get());
			
			if((Boolean)result.get())
				response = ResponseEntity.ok().build();
			else
				response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
		}catch(Exception ex) {
			LOG.error(ex.getMessage());
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		return CompletableFuture.completedFuture(response);
	}
}
