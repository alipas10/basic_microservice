package com.example.Client.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.Client.DTO.ProductDTO;
import com.example.Spring_product.Entity.ProductEntity;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootServer;

	public ConsumerFactory<String, String> consumerFactoryWithGroup(String groupId) {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootServer);
		props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}

	public ConsumerFactory<String, String> consumerFactoryNoGroup() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootServer);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		return new DefaultKafkaConsumerFactory<>(props);
	}
	
	public ConsumerFactory<String, ProductEntity> consumerFactoryReceiveObject() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootServer);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		
		JsonDeserializer<ProductEntity> deserializer = new JsonDeserializer<>(ProductEntity.class);
        deserializer.addTrustedPackages("com.example.Client.Entity"); 
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);

	}

	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory(String groupId) {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryWithGroup(groupId));
		return factory;
	}

	public ConcurrentKafkaListenerContainerFactory<String, String> fooKafkaListenerContainerFactory() {
		return kafkaListenerContainerFactory("foo");
	}

	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerNoGroupId() {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryNoGroup());
		factory.getContainerProperties().setGroupId(UUID.randomUUID().toString());
		return factory;
	}

	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactoryNoGroupId() {
		return kafkaListenerContainerNoGroupId();
	}

	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerNoGroupWithFilterFactory() {

		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactoryNoGroup());
//		could set group id by constant or random id
		factory.getContainerProperties().setGroupId(UUID.randomUUID().toString());
//		filter collection data, get data if fiter == false
		factory.setRecordFilterStrategy(data -> data.value().startsWith("content"));
		return factory;
	}
	
	@Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProductEntity> productKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ProductEntity> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryReceiveObject());
        factory.getContainerProperties().setGroupId(UUID.randomUUID().toString());
        return factory;
    }

}
