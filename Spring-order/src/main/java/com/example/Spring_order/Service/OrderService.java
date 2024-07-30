package com.example.Spring_order.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.example.Spring_order.Client.ProductClient;
import com.example.Spring_order.DTO.ProductDTO;
import com.example.Spring_order.Entity.OrderEntity;
import com.example.Spring_order.Entity.ProductEntity;
import com.example.Spring_order.Repository.OrderRepository;

@Service
public class OrderService {

	Logger log = LoggerFactory.getLogger(OrderService.class);

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

	@KafkaListener(topics = "test", containerFactory = "kafkaMessage")
	public void listenNoGroupWithFilter(@Payload ProductEntity message,
			@Header(name = KafkaHeaders.OFFSET) String topic, Acknowledgment acknowledgment) {
		try {
			log.info(String.format("Message received -----------> %s", message.toString()));
			log.info(String.format("Header of message ----------> %s", topic));
			acknowledgment.acknowledge();
		} catch (Exception e) {
			acknowledgment.nack(Duration.ofMillis(5500));
		}

	}
}
