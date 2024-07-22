package com.example.Spring_product.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import com.example.Spring_product.Entity.ProductEntity;
import com.example.Spring_product.Entity.Test;
import com.example.Spring_product.Repository.ProductRepository;

@Service
public class ProductService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

	@Autowired
	ProductRepository proRepo;

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private KafkaTemplate<String,ProductEntity> kafkaTemplateObject;
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplateMultiType;
	
	
	@Value(value = "${multi-type.topic.name}")
	private String multiTypeTopic;

	public Optional<List<ProductEntity>> getAllProducts() {
		return Optional.ofNullable(proRepo.findAll());
	}

	public Optional<ProductEntity> findById(Long id) {
		return proRepo.findById(id);
	}
	
	public void sendMessage (String topicName, String message) {
        LOGGER.info(String.format("Message sent -> %s", message));
        // send message type asynchronous (non-blocking)
        CompletableFuture<SendResult<String, String>> result = kafkaTemplate.send(topicName,message);
        result.whenComplete((success, failure ) -> {
            if (failure == null) {
                LOGGER.info("Sent message=[" + message + 
                    "] with offset=[" + success.getRecordMetadata().offset() + "]");
            } else {
            	LOGGER.error("Unable to send message=[" + 
                    message + "] due to : " + failure.getMessage());
            }
        });
        // send message type synchronous (blocking)
//        try {
//            kafkaTemplate.send(topicName,message).get(10, TimeUnit.SECONDS);
//        }
//        catch (ExecutionException e) {
//        	LOGGER.error("Unable to send message=[" + message + "] due to : " + e.getCause());
//            
//        }
//        catch (TimeoutException | InterruptedException e) {
//        	LOGGER.error("Unable to send message=[" + message + "] due to : " + e);
//
//        }
	}
	
	public void sendProductById (String topicName, Long id) {
		Optional<ProductEntity> productResult = proRepo.findById(id);
		LOGGER.info(String.format("findProductById -> %s", id));
		productResult.orElseThrow( () -> 
			new RuntimeException("There is error in method sendProductById")
		);
       
        // send message with ProductEntity type asynchronous (non-blocking)
        CompletableFuture<SendResult<String, ProductEntity>> result = kafkaTemplateObject.send(topicName, productResult.get());
        result.whenComplete((success, failure ) -> {
            if (failure == null) {
                LOGGER.info("Sent message=[ topic name = " + topicName + 
                    " | product by Id "+productResult.toString() +" ] with offset=[" + success.getRecordMetadata().offset() + "]");
            } else {
            	LOGGER.error("Unable to send message=[ topic name " + 
                    topicName + "] due to : " + failure.getMessage());
            }
        });
	}
	
	public void sendMultiType() {
		kafkaTemplateMultiType.send(multiTypeTopic,new ProductEntity((long) 1,(long)1,"Product A"));
		kafkaTemplateMultiType.send(multiTypeTopic,new Test("Content A"));
		kafkaTemplateMultiType.send(multiTypeTopic,"String object 3");
	}
}
