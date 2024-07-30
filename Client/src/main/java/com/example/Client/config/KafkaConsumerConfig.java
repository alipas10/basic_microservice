package com.example.Client.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.listener.adapter.RecordFilterStrategy;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

import com.example.Spring_product.Entity.ProductEntity;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
	
	Logger log = LoggerFactory.getLogger(KafkaConsumerConfig.class);

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootServer;

	private Map<String, Object> defaultConfig() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootServer);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		// setting default group id
		props.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
		return props;
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaStringMessage() {
		Map<String, Object> props = defaultConfig();
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

		ConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<>(props);

		ConcurrentKafkaListenerContainerFactory<String, String> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(factory);
		return containerFactory;
	}

	@Bean
	public RecordFilterStrategy<String, String> customFilterStrategy() {
		return record -> {
			String key = record.key();
			String value = record.value();
			return key == null && StringUtils.startsWith(value, "content");
		};
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, ProductEntity> kafkaProductMessage() {
		Map<String, Object> props = defaultConfig();
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

		JsonDeserializer<ProductEntity> deserializer = new JsonDeserializer<>(ProductEntity.class);
		deserializer.addTrustedPackages("com.example.Client.Entity");

		ConcurrentKafkaListenerContainerFactory<String, ProductEntity> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer));
		return factory;
	}

//	@Bean
//	public RecordMessageConverter multiTypeConverter() {
//		StringJsonMessageConverter converter = new StringJsonMessageConverter();
//		DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
//		typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
//		typeMapper.addTrustedPackages("com.example.Spring_product.Entity");
//		Map<String, Class<?>> mappings = new HashMap<>();
//		mappings.put("test", Test.class);
//		mappings.put("product", ProductEntity.class);
//		typeMapper.setIdClassMapping(mappings);
//		converter.setTypeMapper(typeMapper);
//		return converter;
//	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaMultiType() {
		Map<String, Object> props = defaultConfig();
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(JsonDeserializer.TYPE_MAPPINGS,
				"test:com.example.Spring_product.Entity.Test, product:com.example.Spring_product.Entity.ProductEntity");

		ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(props));
		factory.setCommonErrorHandler(errorHandler());
		factory.getContainerProperties().setAckMode(AckMode.RECORD);
		return factory;
	}
	
	public DefaultErrorHandler errorHandler() {
	    BackOff fixedBackOff = new FixedBackOff(1000, 6000);
	    DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, exception) -> {
	    	log.error("There is error at class KafkaConsumerConfig | consuming process message with record "+ consumerRecord.toString());
	    	log.error("There is error at class KafkaConsumerConfig | consuming process message with exception "
	    			+ exception);

	    }, fixedBackOff);
	    errorHandler.addRetryableExceptions(NullPointerException.class);
	    return errorHandler;
	}

}
