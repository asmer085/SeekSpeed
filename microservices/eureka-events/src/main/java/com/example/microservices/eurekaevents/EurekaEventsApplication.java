package com.example.microservices.eurekaevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class EurekaEventsApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaEventsApplication.class, args);
	}

}
