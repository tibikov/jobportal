package org.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.demo.model.Position;
import org.demo.repo.PositionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PositionService {
	
	@Autowired
	private PositionRepository positionRepository;
	
	public List<Position> searchPositions(String keyWord, String location) {
		return new ArrayList<>();
	}
	
	public Position getPosition(Long id) {
		return positionRepository.findById(id).orElse(null);
	}
	
	public Position save(Position position) {				
		return positionRepository.save(position);		
	}
	
	public List<Position> getAll() {
		return StreamSupport.stream(positionRepository.findAll().spliterator(), false).collect(Collectors.toList());
	}
}
