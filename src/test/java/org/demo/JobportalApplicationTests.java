package org.demo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.demo.model.Position;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
class JobportalApplicationTests {

	private static final String STATUS_BAD_REQUEST = "\"status\":\"BAD_REQUEST\"";
	private static final String API_KEY_MISSING = "\"Required request header 'api-key' for method parameter type String is not present\"";
	private static final String API_KEY_VALUE = "1babfc4d-5de7-4dbc-a882-3b24c773016c";
	private static final String API_KEY_NAME = "api-key";

	static boolean isUUID(String value) {
		try {
			UUID.fromString(value);
		} catch (IllegalArgumentException exception) {
			return false;
		}
		return true;
	}

	@Autowired
	WebTestClient webClient;

	@Test
	public void testSaveClient() {
		EntityExchangeResult<byte[]> result = webClient.post().uri("/clients")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.bodyValue("name=testClient&email=testclient%40testcompany.com").exchange().expectStatus().isOk()
				.expectBody().returnResult();
		byte[] content = result.getResponseBodyContent();
		assertNotNull(content);
		assertTrue(isUUID(new String(content, StandardCharsets.UTF_8)));
	}

	@Test
	public void testSavePositionWithoutHeader() {
		EntityExchangeResult<byte[]> result = webClient.post().uri("/positions")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).bodyValue("name=Bar%20Tender&location=Jaszkarajeno")
				.exchange().expectStatus().is4xxClientError().expectBody().returnResult();
		byte[] content = result.getResponseBodyContent();
		assertNotNull(content);
		String body = new String(content, StandardCharsets.UTF_8);
		assertTrue(body.contains(STATUS_BAD_REQUEST) && body.contains(API_KEY_MISSING));
	}

	@Test
	public void testSavePosition() {
		EntityExchangeResult<byte[]> result = webClient.post().uri("/positions")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED).header(API_KEY_NAME, API_KEY_VALUE)
				.bodyValue("name=Bar%20Tender&location=Jaszkarajeno").exchange().expectStatus().isOk().expectBody()
				.returnResult();
		byte[] content = result.getResponseBodyContent();
		assertNotNull(content);
		assertEquals("http://localhost:8085/positions?id=8", new String(content, StandardCharsets.UTF_8));
	}

	@Test
	public void testSearch() {
		EntityExchangeResult<byte[]> result = webClient.get().uri("/search?keyword=dev&location=Szeged")
				.header(API_KEY_NAME, API_KEY_VALUE).exchange().expectStatus().isOk().expectBody().returnResult();
		byte[] content = result.getResponseBodyContent();
		assertNotNull(content);
		System.out.println(new String(content, StandardCharsets.UTF_8));
		assertEquals("[\"http://localhost:8085/positions?id=5\",\"http://localhost:8085/positions?id=6\"]",
				new String(content, StandardCharsets.UTF_8));
	}

	@Test
	public void testSwaggerApi() {
		webClient.get().uri("/v3/api-docs").exchange().expectStatus().isOk();
	}
}
