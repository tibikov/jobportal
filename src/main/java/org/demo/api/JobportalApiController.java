package org.demo.api;

import java.util.HashSet;
import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.demo.model.Client;
import org.demo.model.Position;
import org.demo.service.ClientService;
import org.demo.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
public class JobportalApiController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private PositionService positionService;

	@PostMapping(path = "/clients", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public ResponseEntity<String> client(@RequestBody Client client) {
		return new ResponseEntity<>(clientService.save(client), HttpStatus.OK);
	}

	@GetMapping(path = "/clients")
	public ResponseEntity<List<Client>> getClients() {
		return new ResponseEntity<>(clientService.getAll(), HttpStatus.OK);
	}

	@PostMapping(path = "/positions", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public ResponseEntity<String> postPosition(@RequestHeader("api-key") String apiKey,
			@RequestBody Position position) {
		if(!clientService.isValidApiKey(apiKey)) {
			throw new ConstraintViolationException("Header api-key is invalid", new HashSet<>());
		}
		return new ResponseEntity<>("http://localhost:8085/positions?id=" + positionService.save(position).getId(), HttpStatus.OK);
	}

	@GetMapping(path = "/positions")
	public ResponseEntity<Position> getPosition(@RequestParam Long id) { //@RequestHeader("api-key") String apiKey,
		return new ResponseEntity<>(positionService.getPosition(id), HttpStatus.OK);
	}

	@GetMapping(path = "/positions/all")
	public ResponseEntity<List<Position>> getPosition() {
		return new ResponseEntity<>(positionService.getAll(), HttpStatus.OK);
	}

	@GetMapping(path = "/search")
	public ResponseEntity<List<Position>> search(@Valid @RequestHeader("api-key") String apiKey,
			@RequestParam @NotBlank @Size(max = 50) String keyWord,
			@RequestParam @NotBlank @Size(max = 50) String location) {
		return new ResponseEntity<>(positionService.searchPositions(keyWord, location), HttpStatus.OK);
	}

}