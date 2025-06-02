package com.tutorial.tutorialwebflux;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ReflectiveScan;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import com.tutorial.tutorialwebflux.handlers.BeneficiariesHandlers;
import com.tutorial.tutorialwebflux.handlers.GetByIdContactsHandlers;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@ComponentScan(basePackages = {"com.tutorial.tutorialwebflux.configurations",
							   "com.tutorial.tutorialwebflux.handlers",
							   "com.tutorial.tutorialwebflux.messages",
							   "com.tutorial.tutorialwebflux.models",
							   "com.tutorial.tutorialwebflux.services",
							   "com.tutorial.tutorialwebflux.controllers",
							   "com.tutorial.tutorialwebflux.filters",
							   "package com.tutorial.tutorialwebflux.daos"
							})
@ReflectiveScan(basePackages = {"com.tutorial.tutorialwebflux.configurations",
								"com.tutorial.tutorialwebflux.handlers",
								"com.tutorial.tutorialwebflux.messages",
								"com.tutorial.tutorialwebflux.models",
								"com.tutorial.tutorialwebflux.services",
								"com.tutorial.tutorialwebflux.controllers",
								"com.tutorial.tutorialwebflux.filters",
								"package com.tutorial.tutorialwebflux.daos"
								})
@EnableWebFlux
@EnableR2dbcRepositories
@SpringBootApplication
@EnableCaching
public class TutorialWebfluxApplication {

	public static void main(String[] args) {
		SpringApplication.run(TutorialWebfluxApplication.class, args);
	}

	@Bean
  	public RouterFunction<ServerResponse> route(
		GetByIdContactsHandlers getByIdContactsHandlers,
		BeneficiariesHandlers beneficiariesHandlers) {

    return RouterFunctions.route()
	  .GET("/contacts/{id}", getByIdContactsHandlers::getById)
	  .GET("/beneficiaries/{id}", beneficiariesHandlers::getBeneficiaryById)
	  .POST("/beneficiaries/{id}", accept(MediaType.MULTIPART_FORM_DATA), beneficiariesHandlers::saveBeneficiariesByFile)
	  //.POST("/beneficiaries-batch/{id}", accept(MediaType.MULTIPART_FORM_DATA), beneficiariesHandlers::saveBeneficiariesByFileBatch)
	  .build();
  	}
  
}
