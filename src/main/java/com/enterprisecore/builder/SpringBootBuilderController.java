package com.enterprisecore.builder;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
	
	@PostMapping(path="/spring-boot-app", consumes="application/json")
	public CompletableFuture<ResponseEntity<?>> generateSpringBootApp(
			@RequestBody Optional<APIApplication> apiApp){
		ResponseEntity<?> response = null;
		try {
			boolean result = false;
			if(apiApp.isPresent())
				result = appGenService.generateSpringBootApp(apiApp.get());
			 
			if(result)
				response = ResponseEntity.ok().build();
			else
				response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			
		}catch(Exception ex) {
			ex.printStackTrace();
			response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
		
		return CompletableFuture.completedFuture(response);
	}
}
