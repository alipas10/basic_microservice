package com.example.Spring_product.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.example.Spring_product.Entity.ProductEntity;

@Configuration
public class KafkaProducerConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootKafkaServer;

	public Map<String, Object> producerDefaultConfigs() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootKafkaServer);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return configProps;
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		Map<String, Object> props = producerDefaultConfigs();
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		ProducerFactory<String, String> producerFactory = new DefaultKafkaProducerFactory<>(props);
		return new KafkaTemplate<>(producerFactory);
	}

	@Bean
	public KafkaTemplate<String, ProductEntity> kafkaTemplateSendObject() {
		Map<String, Object> props = producerDefaultConfigs();
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		ProducerFactory<String, ProductEntity> producerFactory = new DefaultKafkaProducerFactory<>(props);
		return new KafkaTemplate<>(producerFactory);
	}

	@Bean
	public KafkaTemplate<String, Object> multiTypeKafkaTemplate() {
		Map<String, Object> configProps = producerDefaultConfigs();
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

		configProps.put(JsonSerializer.TYPE_MAPPINGS,
				"test:com.example.Spring_product.Entity.Test, product:com.example.Spring_product.Entity.ProductEntity");

		ProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(configProps);
		return new KafkaTemplate<>(factory);
	}

	@Bean
	//@Scope("prototype")
	public KafkaProducer<String, Object> sendMessageWithTransaction() {
		Map<String, Object> configProps = producerDefaultConfigs();
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);
		configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "test_id");
		configProps.put(ProducerConfig.TRANSACTION_TIMEOUT_CONFIG, "20000");
		configProps.put(ProducerConfig.RETRIES_CONFIG, "5");
		// can setting = 1
		configProps.put(ProducerConfig.ACKS_CONFIG, "-1");
		configProps.put(JsonSerializer.TYPE_MAPPINGS, "product:com.example.Spring_product.Entity.ProductEntity");
		KafkaProducer<String, Object> producer = new KafkaProducer<>(configProps);
		return producer;
	}

}
