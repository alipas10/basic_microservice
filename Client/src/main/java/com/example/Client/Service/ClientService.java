package com.example.Client.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.Client.DTO.ClientDTO;
import com.example.Client.DTO.ProductDTO;
import com.example.Client.Entity.ClientEntiry;
import com.example.Client.Repository.ClientRepository;

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

	@KafkaListener(topics = "${kafka.topic.topic1}", groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory")
	public void consumer(String message) {
		LOGGER.info(String.format("Message received -> %s", message));
	}
}
