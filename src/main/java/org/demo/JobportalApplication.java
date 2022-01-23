package org.demo;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class JobportalApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobportalApplication.class, args);
	}

	
	@Bean
	public GroupedOpenApi publicApi() {
		return GroupedOpenApi.builder()
				.group("jobportal")
				//.pathsToMatch("/*")
				.addOpenApiCustomiser(openApi -> openApi.info(new Info().title("Jobportal API")))
				.packagesToScan("org.demo")
				.build();
	}
}

// http://localhost:8085/swagger-ui/index.html
// http://localhost:8085/v3/api-docs

// use searches repository
// introduce graphql
// enrich positions (description, client intro, expectation, tags) 
// maintenance: positions expiring positions, statistics
// appliacants vs /clients api
// use client secret: https://www.oauth.com/oauth2-servers/client-registration/registering-new-application/
// use PagingAndSortingRepository for Positions
// use hibernate with real database, implementation of repositories
// use oath for authentication

// PRG POST/REDIRECT/GET flow to avoid duplicated submission, using flash attributes
// annotation !prod?
// BasicAuthentication? OAUTH 2.0 + keycloak

// error handling
// logging (slf4j + log4j2)
// testing
// docker deployment
// observability (opentelemetry)
// hibernate with real database
// documentation
