package com.example.Spring_order.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.Spring_order.DTO.ProductDTO;

@FeignClient(name = "product-service", path = "http://localhost:8081")
public interface ProductClient {

	@GetMapping(path = "/products/getProduct/{id}")
	ProductDTO getProductById (@PathVariable(name = "id") String id);
}
