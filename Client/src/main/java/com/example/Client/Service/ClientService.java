package com.example.Client.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.Client.DTO.ClientDTO;
import com.example.Client.DTO.ProductDTO;
import com.example.Client.Entity.ClientEntiry;
import com.example.Client.Repository.ClientRepository;
import com.example.Spring_product.Entity.ProductEntity;

@Service
public class ClientService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClientService.class);

	@Autowired
	ClientRepository clientRepo;

	@Autowired
	RestTemplate restTemplate;

//	@Value(value = "${url.default_product_service}")
//	private String urlProduct;
//	
//	private static final String URL_GET_PRODUCT = "/products/getProduct/"; 

	@Autowired
	WebClient webClient;

	public Optional<List<ClientEntiry>> getAllClients() {
		List<ClientEntiry> lst = clientRepo.findAll();
		return Optional.ofNullable(lst);
	}

	public Optional<ClientEntiry> findClientById(Long id) throws Exception {
		Optional<ClientEntiry> cliResult = clientRepo.findById(id);
		ResponseEntity<ProductDTO> responseEntity = restTemplate.getForEntity(
				"http://product-service/products/getProduct/" + cliResult.get().getProductId(), ProductDTO.class);

		ProductDTO proDTO = responseEntity.getBody();
		if (Optional.ofNullable(proDTO).isEmpty()) {
			throw new Exception("Product id not exists !");
		}

		return clientRepo.findById(id);

	}

	public Optional<ClientEntiry> saveClient(ClientDTO cltDTO) throws Exception {

		ProductDTO proDTO = webClient.get().uri("http://product-service/products/getProduct/" + cltDTO.getProductId())
				.retrieve().bodyToMono(ProductDTO.class).block();

		if (Optional.ofNullable(proDTO).isEmpty()) {
			throw new Exception("Product id not found !");
		}
		ClientEntiry result = clientRepo
				.save(new ClientEntiry(cltDTO.getId(), cltDTO.getName(), cltDTO.getEmail(), cltDTO.getProductId()));
		return Optional.ofNullable(result);

	}

//	@KafkaListener(topics = "topicInconrrect", groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
//	public void consumer(@Payload String message, @Header(KafkaHeaders.RECEIVED_TOPIC) String header) {
//		LOGGER.info(String.format("Message received -> %s", message));
//		LOGGER.info(String.format("Header of message -> %s", header));
//
//	}

//	@KafkaListener(topicPartitions = @TopicPartition(topic = "topic1", partitionOffsets = {
////			topicPartitions  = @TopicPartition(topic = "topic1", partitions = { "0", "1" }))
//			@PartitionOffset(partition = "0", initialOffset = "0"),
//			@PartitionOffset(partition = "3", initialOffset = "0") }), containerFactory = "partitionsKafkaListenerContainerFactory")
//	public void listenToPartition(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
//		LOGGER.info(String.format("Message received -> %s", message));
//		LOGGER.info(String.format("Header of message -> %s", partition));
//	}
	
//	@KafkaListener(topics = "${kafka.topic.topic1}", containerFactory = "kafkaListenerContainerNoGroupWithFilterFactory")
//	public void listenNoGroupWithFilter(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
//		LOGGER.info(String.format("Message received -> %s", message));
//		LOGGER.info(String.format("Header of message -> %s", partition));
//	}
	
	@KafkaListener(topics = "${product.topic.name}", containerFactory = "productKafkaListenerContainerFactory")
	public void listenNoGroupWithFilter(@Payload ProductEntity product, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
		LOGGER.info("Object message receive ---------------------->" +  product.toString());
		LOGGER.info(String.format("Header of message -> %s", topic));
	}
}
