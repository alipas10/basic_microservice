package com.example.Spring_product.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
public class KafkaTopicConfig {

	@Value(value = "${spring.kafka.bootstrap-servers}")
	private String bootstrapAddress;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);

		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic topic1() {
		return new NewTopic("topic1", 1, (short) 1);
	}

	/**
	 * 
	 * assignReplicas(partition, list replica) ->  assign replicas for the partition of topic 
	 * replicas(1) -> config replica count property for topic name("topic1") ->
	 * config topic name partitions(1) -> config partition for topic
	 * TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd" -> choose compresstion topic type
	 * = zstd otherwise Gzip, Snappy, LZ4...
	 * 
	 * @return create automatically topic on kafka
	 */
	// @formatter:off
//	@Bean
//	public NewTopic topic2() {
//		return TopicBuilder.name("topic1")
//					.partitions(1)
//					.replicas(1)
//					.config(TopicConfig.COMPRESSION_TYPE_CONFIG, "zstd")
//					.compact()
//				.build();
//	}
	// @formatter:on

}
