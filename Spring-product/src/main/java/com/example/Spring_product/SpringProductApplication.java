package com.example.Spring_product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SpringProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringProductApplication.class, args);
	}

}
