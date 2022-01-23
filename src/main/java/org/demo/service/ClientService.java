package org.demo.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.demo.model.Client;
import org.demo.repo.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService {
	
	@Autowired
	private ClientRepository clientRepository;
	
	public String save(Client client) {		
		return clientRepository.save(client).getApiKey();
	}
	
	public Client findByEmail(String email) {
		return clientRepository.findByEmail(email);
	}
	
	public List<Client> getAll() {
		return StreamSupport.stream(clientRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}
	
	public boolean isValidApiKey(String apiKey) {
		return apiKey != null && clientRepository.findByApiKey(apiKey) != null;
	}
	
}


