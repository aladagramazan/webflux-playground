package com.remaladag.webflux_playground.sec05;

import com.remaladag.webflux_playground.sec05.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = {
        "sec=sec05"
        // "logging.level.org.springframework.r2dbc=DEBUG"
})
public class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(com.remaladag.webflux_playground.sec04.CustomerServiceTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void unauthorized() {
        // no token
        webTestClient.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);

        // invalid token
        this.validateGet("invalid-token", HttpStatus.UNAUTHORIZED);
    }

    @Test
    public void standardCategory() {
        this.validateGet("secret321", HttpStatus.OK);
        this.validatePost("secret321", HttpStatus.FORBIDDEN);
    }

    @Test
    public void primeCategory() {
        this.validateGet("secret654", HttpStatus.OK);
        this.validatePost("secret654", HttpStatus.OK);
    }

    private void validateGet(String token, HttpStatus expectedStatus) {
        webTestClient.get()
                .uri("/customers")
                .header("auth-token", token)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    private void validatePost(String token, HttpStatus expectedStatus) {
        var customer = new CustomerDto(null, "aladag", "aladag@aladag");
        webTestClient.post()
                .uri("/customers")
                .header("auth-token", token)
                .bodyValue(customer)
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }
}
