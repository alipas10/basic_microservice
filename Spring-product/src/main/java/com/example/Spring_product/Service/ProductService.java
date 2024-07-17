package com.example.Spring_product.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Spring_product.Entity.ProductEntity;
import com.example.Spring_product.Repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	ProductRepository proRepo;

	public Optional<List<ProductEntity>> getAllProducts() {
		return Optional.ofNullable(proRepo.findAll());
	}

	public Optional<ProductEntity> findById(Long id) {
		return proRepo.findById(id);
	}
}
