package com.example.Client.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.Spring_product.Entity.ProductEntity;
import com.example.Spring_product.Entity.Test;

@Component
@KafkaListener(topics = "multiType", containerFactory = "kafkaMultiType")
public class MultiTypeListenerService {

	Logger log = LoggerFactory.getLogger(MultiTypeListenerService.class);

	@KafkaHandler
	public void handleGreeting(Test test) {
		log.info("-------------> Test object received:     " + test.toString());
	}

	@KafkaHandler
	public void handleF(ProductEntity product) {
		log.info("-------------> Product object received:     " + product.toString());
	}

	@KafkaHandler(isDefault = true)
	public void unknown(Object object) {
		log.info("-------------> Unknown object received:     " + object.toString());
	}

}
