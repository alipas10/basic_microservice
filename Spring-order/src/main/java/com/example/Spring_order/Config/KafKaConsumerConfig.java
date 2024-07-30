package com.example.Spring_order.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.example.Spring_order.Entity.ProductEntity;

@EnableKafka
@Configuration
public class KafKaConsumerConfig {

	Logger log = LoggerFactory.getLogger(KafKaConsumerConfig.class);

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
	public ConcurrentKafkaListenerContainerFactory<String, ProductEntity> kafkaMessage() {
		Map<String, Object> props = defaultConfig();
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
		props.put(JsonDeserializer.TYPE_MAPPINGS, "product:com.example.Spring_order.Entity.ProductEntity");
		props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "2000");
		props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, false);

		
		ConsumerFactory<String, ProductEntity> factory = new DefaultKafkaConsumerFactory<>(props,
				new StringDeserializer(), new JsonDeserializer<>(ProductEntity.class));

		ConcurrentKafkaListenerContainerFactory<String, ProductEntity> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
		containerFactory.setConsumerFactory(factory);
		containerFactory.getContainerProperties().setAckMode(AckMode.MANUAL);
		return containerFactory;
	}

}
