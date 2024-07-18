package com.example.Spring_product.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Spring_product.Entity.ProductEntity;
import com.example.Spring_product.Service.ProductService;

@RestController
@RequestMapping(path = "/products")
public class ProductController {
	
	@Autowired
	ProductService proService;
	
	@GetMapping(path = "/getAll")
	public ResponseEntity<?> getAllProducts (){
		Optional<?> result = proService.getAllProducts(); 
		if(result.isEmpty())
				return ResponseEntity.badRequest().body("There is error occur !");
		return ResponseEntity.ok(result.get());
	}

	@GetMapping(path = "/getProduct/{id}")
	public ResponseEntity<?> getProduct ( @PathVariable(required = true) Long id){
		Optional<ProductEntity> result = proService.findById(id);
		if(result.isEmpty())
			return ResponseEntity.badRequest().body("There is error occur !");
		return ResponseEntity.ok(result.get());
	}
}