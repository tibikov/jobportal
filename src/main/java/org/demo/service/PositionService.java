package org.demo.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.demo.model.Position;
import org.demo.repo.PositionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Service
@Transactional
public class PositionService {

	Logger logger = LoggerFactory.getLogger(ClientService.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private PositionRepository positionRepository;

	public Position getPosition(Long id) {
		Position result = positionRepository.findById(id).orElse(null);
		logger.info("Position found for id: {}, is: {}", id, result == null ? "none" : result);
		return result;
	}

	public Position save(Position position) {
		Position result = positionRepository.save(position);
		logger.info("Position saved: {}", result);
		return result;
	}

	public List<Position> getAll() {
		return StreamSupport.stream(positionRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}

	public List<Position> searchPositions(String keyWord, String location) {
		List<Position> result = entityManager.createNamedQuery("findAllPositionsWithNameAndLocation", Position.class)
				.setParameter("pName", keyWord).setParameter("pLocation", location).getResultList();

		String resStr = result.stream().map(Object::toString).collect(Collectors.joining(", "));
		logger.info("Positions found for keyWord: '{}' and location: '{}' are: {}", keyWord, location, resStr);
		return result;
	}

	public List<String> searchPositionsAsUrl(String keyword, String location,
			String positionBaseUri) {		
		return searchPositions(keyword, location).stream().map(position -> positionBaseUri + "=" + position.getId())
				.collect(Collectors.toList());
	}
}
