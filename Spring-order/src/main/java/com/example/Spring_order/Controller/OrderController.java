package com.example.Spring_order.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Spring_order.Entity.OrderEntity;
import com.example.Spring_order.Service.OrderService;

@RestController
@RequestMapping(path = "/orders")
public class OrderController {

	@Autowired
	private OrderService orderService;

	@GetMapping
	public ResponseEntity<?> getAllOrders() {
		Optional<List<OrderEntity>> result = orderService.getAll();
		if (result.isEmpty()) {
			return ResponseEntity.badRequest().body("There is error occur !");
		}
		return ResponseEntity.ok(result.get());
		
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getOrderById(@PathVariable String id) {
		Optional<OrderEntity> result = orderService.findById(Long.getLong(id));
		if (result.isEmpty()) {
			return ResponseEntity.badRequest().body("There is error occur !");
		}
		return ResponseEntity.ok(result.get());
	}

	@PostMapping
	public ResponseEntity<?> addOrder(@RequestBody OrderEntity order) {
		Optional<OrderEntity> result = orderService.addOrder(order);
		if (result.isEmpty()) {
			return ResponseEntity.badRequest().body("There is error occur !");
		}
		return ResponseEntity.ok(result.get());
	}

}
