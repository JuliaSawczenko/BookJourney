package com.bookJourney.springboot;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition
@SpringBootApplication
public class BookJourneyApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookJourneyApplication.class, args);
	}
}
