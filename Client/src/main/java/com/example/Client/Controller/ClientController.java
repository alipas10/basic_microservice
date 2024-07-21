package com.example.Client.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Client.DTO.ClientDTO;
import com.example.Client.Entity.ClientEntiry;
import com.example.Client.Service.ClientService;

@RestController
@RequestMapping(path = "/clients")
public class ClientController {

	@Autowired
	ClientService cltService;
	
	@PostMapping(path = "/create-client")
	public ResponseEntity<?> createClient(@RequestBody ClientDTO cltDTO) {
		Optional<ClientEntiry> result = Optional.empty();
 		try {
			result = cltService.saveClient(cltDTO);
			if (result.isEmpty()) {
				return ResponseEntity.badRequest().body("There is error occur !");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Product id not found !");
		}
	
		return ResponseEntity.status(HttpStatus.CREATED).body(result.get());
	}
	
	@GetMapping(path = "/find-client")
	public ResponseEntity<?> createClient(@RequestParam(name = "id") Long id) throws Exception {
		Optional<ClientEntiry> result = cltService.findClientById(id);
		if (result.isEmpty()) {
			return ResponseEntity.badRequest().body("There is error occur !");
		}
		return ResponseEntity.ok(result.get());
	}
	
}

