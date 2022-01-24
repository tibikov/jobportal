package org.demo.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.demo.model.Client;
import org.demo.repo.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientService {

	Logger logger = LoggerFactory.getLogger(ClientService.class);

	@Autowired
	private ClientRepository clientRepository;

	public String save(Client client) {
		String apiKey = clientRepository.save(client).getApiKey();
		logger.info("Client {} saved", client);
		return apiKey;
	}

	public Client findByEmail(String email) {
		logger.info("Searching by email: {}", email);
		return clientRepository.findByEmail(email);
	}

	public List<Client> getAll() {
		logger.info("Getting all clients");
		return StreamSupport.stream(clientRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	public boolean isValidApiKey(String apiKey) {
		boolean valid = apiKey != null && clientRepository.findByApiKey(apiKey) != null;
		logger.info("Checked apiKey {}, it is {}", apiKey, valid ? "valid" : "invalid");
		return valid;
	}

}
