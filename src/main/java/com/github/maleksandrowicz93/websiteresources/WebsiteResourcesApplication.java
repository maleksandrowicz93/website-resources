package com.github.maleksandrowicz93.websiteresources;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.github.maleksandrowicz93.websiteresources.repository.jpa")
@EnableMongoRepositories(basePackages = "com.github.maleksandrowicz93.websiteresources.repository.mongo")
public class WebsiteResourcesApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebsiteResourcesApplication.class, args);
	}
}
