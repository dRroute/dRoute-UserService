package com.droute.userservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableDiscoveryClient
public class DrouteUserServiceApplication {

	private static final Logger logger = LoggerFactory.getLogger(DrouteUserServiceApplication.class);

    public static void main(String[] args) {

    	// Load environment variables from .env file
    	Dotenv dotenv = Dotenv.configure().load();
        dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

        SpringApplication.run(DrouteUserServiceApplication.class, args);
        logger.info("Started DrouteUserServiceApplication successfully...");
    }

}
