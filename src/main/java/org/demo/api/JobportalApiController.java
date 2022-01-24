package org.demo.api;

import java.util.List;

import javax.validation.constraints.Size;

import org.demo.model.Client;
import org.demo.model.Position;
import org.demo.service.ClientService;
import org.demo.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@Validated
public class JobportalApiController {

	@Autowired
	private ClientService clientService;

	@Autowired
	private PositionService positionService;

	@PostMapping(path = "/clients", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public ResponseEntity<String> client(@RequestBody Client client) {
		return new ResponseEntity<>(clientService.save(client), HttpStatus.OK);
	}

	@PostMapping(path = "/positions", consumes = { MediaType.APPLICATION_FORM_URLENCODED_VALUE })
	public ResponseEntity<String> postPosition(@RequestHeader("api-key") String apiKey,
			@RequestBody Position position) {
		if (!clientService.isValidApiKey(apiKey)) {
			throw new JobportalInvalidApikeyException(apiKey);
		}
		Long id = positionService.save(position).getId();
		String url = ServletUriComponentsBuilder.fromCurrentRequest().queryParam("id", id).toUriString();
		return new ResponseEntity<>(url, HttpStatus.OK);
	}

	@GetMapping(path = "/positions")
	public ResponseEntity<Position> getPosition(@RequestHeader("api-key") String apiKey, @RequestParam Long id) {
		if (!clientService.isValidApiKey(apiKey)) {
			throw new JobportalInvalidApikeyException(apiKey);
		}
		return new ResponseEntity<>(positionService.getPosition(id), HttpStatus.OK);
	}

	@GetMapping(path = "/search")
	public ResponseEntity<List<String>> search(@RequestHeader("api-key") String apiKey,
			@RequestParam @Size(max = 50) String keyword, @RequestParam @Size(max = 50) String location) {
		if (!clientService.isValidApiKey(apiKey)) {
			throw new JobportalInvalidApikeyException(apiKey);
		}
		String positionBaseUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/positions").queryParam("id").toUriString();
		List<String> positionUrls = positionService.searchPositionsAsUrl(keyword, location, positionBaseUri);
		return new ResponseEntity<>(positionUrls, HttpStatus.OK);
	}

}
