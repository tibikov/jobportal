package org.demo;

import org.demo.model.Client;
import org.demo.model.Position;
import org.demo.repo.ClientRepository;
import org.demo.repo.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JobportalRunner implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(JobportalRunner.class);

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Override
	public void run(String... args) throws Exception {

		logger.info("Loading initial data into repositories");

		var c1 = new Client("first_client", "app@firstCompany.org", "1babfc4d-5de7-4dbc-a882-3b24c773016c");		
		clientRepository.save(c1);

		var c2 = new Client("second_client", "app@secondCompany.org", "0347ac0a-9d67-48c2-8f55-d221567222d2");		
		clientRepository.save(c2);

		var p1 = new Position("queen", "London, UK");
		positionRepository.save(p1);

		var p2 = new Position("bar tender", "Jászkarajenő, HU");
		positionRepository.save(p2);

		clientRepository.findAll().forEach((client) -> logger.info("{}", client));

		positionRepository.findAll().forEach((position) -> logger.info("{}", position));

	}
}
