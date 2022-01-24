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

		clientRepository
				.save(new Client("first_client", "app@firstCompany.org", "1babfc4d-5de7-4dbc-a882-3b24c773016c"));
		clientRepository
				.save(new Client("second_client", "app@secondCompany.org", "0347ac0a-9d67-48c2-8f55-d221567222d2"));

		positionRepository.save(new Position("Queen", "London"));
		positionRepository.save(new Position("Java Developer", "Budapest"));
		positionRepository.save(new Position("Senior Java Developer", "Budapest"));
		positionRepository.save(new Position("Junior C# Developer", "Budapest"));
		positionRepository.save(new Position("Full-stack Developer ", "Szeged"));
		positionRepository.save(new Position("Development Manager", "Szeged"));
		positionRepository.save(new Position("Quality Manager", "Szeged"));

		clientRepository.findAll().forEach((client) -> logger.info("{}", client));

		positionRepository.findAll().forEach((position) -> logger.info("{}", position));

	}
}
