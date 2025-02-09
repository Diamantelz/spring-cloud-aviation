package ru.itis.flightavailabilityservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class FlightAvailabilityServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlightAvailabilityServiceApplication.class, args);
	}
}
