package com.example.Spring_product.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.Spring_product.Entity.ProductEntity;
import com.example.Spring_product.Entity.Test;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

@Configuration
public class KafkaProducerConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootKafkaServer;

	@Bean
	public Map<String, Object> producerConfigs() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootKafkaServer);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "20971520");
		// See https://kafka.apache.org/documentation/#producerconfigs for more
		// properties
		return configProps;
	}

	@Bean
	public Map<String, Object> producerConfigJsonSerializer() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootKafkaServer);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return configProps;
	}

	@Bean
	public ProducerFactory<String, String> producerFactory() {
		return new DefaultKafkaProducerFactory<>(producerConfigs());
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

	@Bean
	public ProducerFactory<String, ProductEntity> producerFactoryJson() {
		return new DefaultKafkaProducerFactory<>(producerConfigJsonSerializer());
	}

	@Bean
	public KafkaTemplate<String, ProductEntity> kafkaTemplateSendObject() {
		return new KafkaTemplate<>(producerFactoryJson());
	}

	@Bean
	public ProducerFactory<String, Object> multiTypeProducerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootKafkaServer);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		
		
        configProps.put(JsonSerializer.TYPE_MAPPINGS, 
        		"test:com.example.Spring_product.Entity.Test, product:com.example.Spring_product.Entity.ProductEntity");

//		configProps.put(JsonSerializer.TYPE_MAPPINGS,
//				"productEntity:com.example.Spring_product.Entity.ProductEntity, test:com.example.Spring_product.Entity.Test");
		// Tạo ObjectMapper và cấu hình DefaultJackson2JavaTypeMapper
//	    ObjectMapper objectMapper = new ObjectMapper();
//	    
//	    DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
//	    typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
//	    typeMapper.addTrustedPackages("com.example.Spring_product.Entity");
//	    Map<String, Class<?>> mappings = new HashMap<>();
//	    mappings.put("test", Test.class);
//	    mappings.put("productEntity", com.example.Spring_product.Entity.ProductEntity.class);
//	    
//	    typeMapper.setIdClassMapping(mappings);
//	    
//	    
//	    objectMapper.setDefaultTyping((TypeResolverBuilder<?>) typeMapper); 
//	    JsonSerializer<Object> jsonSerializer = new JsonSerializer<>(objectMapper); 
	    //	    PolymorphicTypeValidator validator = 
//	    	    BasicPolymorphicTypeValidator.builder()
//	    	        .allowIfSubType(Object.class) // Cho phép tất cả subtype của Object
//	    	        .build();
//	    objectMapper.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL);
//	    objectMapper.activateDefaultTypingAsProperty(typeMapper, ObjectMapper.DefaultTyping.NON_FINAL, "_class");
	    // Sử dụng ObjectMapper đã cấu hình cho JsonSerializer
//	    configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//	    JsonSerializer<Object> jsonSerializer = new JsonSerializer<>(objectMapper); // Truyền objectMapper vào constructor
	    
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, Object> multiTypeKafkaTemplate() {
		return new KafkaTemplate<>(multiTypeProducerFactory());
	}
}
