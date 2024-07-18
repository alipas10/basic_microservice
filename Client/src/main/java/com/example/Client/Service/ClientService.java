package com.example.Client.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.Client.DTO.ClientDTO;
import com.example.Client.DTO.ProductDTO;
import com.example.Client.Entity.ClientEntiry;
import com.example.Client.Repository.ClientRepository;

@Service
public class ClientService {

	@Autowired
	ClientRepository clientRepo;
	
	@Autowired
	RestTemplate restTemplate;

	public Optional<List<ClientEntiry>> getAllClients() {
		List<ClientEntiry> lst = clientRepo.findAll();
		return Optional.ofNullable(lst);
	}

	public Optional<ClientEntiry> findClientById(Long id) throws Exception {
		Optional<ClientEntiry> cliResult =  clientRepo.findById(id);
		
		ResponseEntity<ProductDTO> responseEntity = restTemplate
                .getForEntity("http://localhost:8081/products/getProduct/" + cliResult.get().getProductId(),
                ProductDTO.class);
		
		ProductDTO proDTO = responseEntity.getBody();
		if (Optional.ofNullable(proDTO).isEmpty()) {
			throw new  Exception("Product id not exists !");
		}
		
		return clientRepo.findById(id);

	}

	public Optional<ClientEntiry> saveClient(ClientDTO cltDTO) {
		ClientEntiry result = clientRepo.save(new ClientEntiry(cltDTO.getId(), cltDTO.getName()
				, cltDTO.getEmail(), cltDTO.getProductId()));
		return Optional.ofNullable(result);

	}
}
