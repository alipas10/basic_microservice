package com.example.Spring_order.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Spring_order.Client.ProductClient;
import com.example.Spring_order.DTO.ProductDTO;
import com.example.Spring_order.Entity.OrderEntity;
import com.example.Spring_order.Repository.OrderRepository;

@Service
public class OrderService {

	@Autowired
	OrderRepository orderRepo;

	@Autowired
	ProductClient proClient;

	public Optional<List<OrderEntity>> getAll() {
		return Optional.ofNullable(orderRepo.findAll());
	}

	public Optional<OrderEntity> findById(Long id) {
		return orderRepo.findById(id);
	}

	public Optional<OrderEntity> addOrder(OrderEntity order) {
		ProductDTO product = proClient.getProductById(order.getProductId());
		if (Optional.ofNullable(product).isEmpty()) {
			throw new RuntimeException("Product not found");
		} else {
			return Optional.ofNullable(orderRepo.save(order));
		}

	}
}
