package com.example.lab3;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class Lab3ApplicationTests {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void contextLoads() {
	}

	@Test
	void testAuthorEndpoint() {
		String url = "http://localhost:" + port + "/author";
		String response = this.restTemplate.getForObject(url, String.class);
		assertThat(response).isEqualTo("Drozdov Alexandr IP-217");
	}

	@Test
	void testAuthorEndpointWithParam() {
		String url = "http://localhost:" + port + "/author?name=Asxab Tamaev";
		String response = this.restTemplate.getForObject(url, String.class);
		assertThat(response).isEqualTo("Asxab Tamaev");
	}
}
