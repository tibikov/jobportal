package org.demo;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.demo.model.Client;
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
	public void findAllClients() throws Exception {
		webClient.get().uri("/clients").exchange().expectStatus().isOk();
	}

	@Test
	public void findAllPositions() throws Exception {
		webClient.get().uri("/positions/all").exchange().expectStatus().isOk();
	}

	@Test
	public void testRegisterClient() {
		Client client = new Client();
		client.setName("TestClient");
		client.setEmail("TestClient@testcompany.com");
		EntityExchangeResult<byte[]> result = webClient.post().uri("/clients")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.bodyValue("name=testClient&email=testclient%40testcompany.com").exchange().expectStatus().isOk()
				.expectBody().returnResult();
		byte[] content = result.getResponseBodyContent();
		assertNotNull(content);
		assertTrue(isUUID(new String(content, StandardCharsets.UTF_8)));
	}

//	@Test
//	public void testSearch() {
//		final Response response = givenAuth().get(URL_PREFIX + "/search");
//		assertEquals(200, response.statusCode());
//		System.out.println(response.asString());
//	}

	@Test
	public void testSwaggerApi() {
		webClient.get().uri("/v3/api-docs").exchange().expectStatus().isOk();
	}
}
