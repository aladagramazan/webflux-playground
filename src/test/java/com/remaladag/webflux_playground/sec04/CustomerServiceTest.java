package com.remaladag.webflux_playground.sec04;

import com.remaladag.webflux_playground.sec04.dto.CustomerDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest(properties = {
        "sec=sec04"
        // "logging.level.org.springframework.r2dbc=DEBUG"
})
public class CustomerServiceTest {

    //integration test
    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetAllCustomers() {
        webTestClient.get()
                .uri("/customers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType("application/json")
                .expectBodyList(CustomerDto.class)
                .value(list -> log.info("list : {}", list))
                .hasSize(22);
    }

    @Test
    public void testPaginatedCustomers() {
        webTestClient.get()
                .uri("/customers/paginated?page=3&size=2")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response -> log.info("response : {}", new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$[0].id").isEqualTo(8)
                .jsonPath("$[1].id").isEqualTo(10);
    }

    @Test
    public void testCustomerById() {
        webTestClient.get()
                .uri("/customers/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response -> log.info("response : {}", new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam")
                .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    public void createAndDeleteCustomer() {
        var customerDto = new CustomerDto(null, "test", "test@gmail.com");
        webTestClient.post()
                .uri("/customers")
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response -> log.info("response : {}", new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.id").isNotEmpty()
                .jsonPath("$.name").isEqualTo("test")
                .jsonPath("$.email").isEqualTo("test@gmail.com");

        // delete
        webTestClient.delete()
                .uri("/customers/{id}", 28)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();
    }

    @Test
    public void updateCustomer() {
        var customerDto = new CustomerDto(null, "emily2", "emily@example.com2");
        webTestClient.put()
                .uri("/customers/{id}", 4)
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response -> log.info("response : {}", new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(4)
                .jsonPath("$.name").isEqualTo("emily2")
                .jsonPath("$.email").isEqualTo("emily@example.com2");
    }

    @Test
    public void testCustomerNotFound() {
        // get
        webTestClient.get()
                .uri("/customers/1000")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id: 1000] is not found");

        // update
        var customerDto = new CustomerDto(null, "emily2", "emily2@gmail.com");
        webTestClient.put()
                .uri("/customers/{id}", 1000)
                .bodyValue(customerDto)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id: 1000] is not found");

        // delete
        webTestClient.delete()
                .uri("/customers/{id}", 1000)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id: 1000] is not found");
    }

    @Test
    public void invalidInput() {
        // missing name
        var missingName = new CustomerDto(null, null, "emily2@gmail.com");
        webTestClient.post()
                .uri("/customers")
                .bodyValue(missingName)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Name is required");

        // missing email
        var missingEmail = new CustomerDto(null, "emily2", null);
        webTestClient.post()
                .uri("/customers")
                .bodyValue(missingEmail)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Valid email is required");

        // invalid mail
        var invalidEmail = new CustomerDto(null, "emily2", "emily2");
        webTestClient.post()
                .uri("/customers")
                .bodyValue(invalidEmail)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Valid email is required");

        // update
        var missingUpdateName = new CustomerDto(null, null, "emily2@gmail.com");
        webTestClient.put()
                .uri("/customers/{id}", 7)
                .bodyValue(missingUpdateName)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Name is required");

        var missingUpdateEmail = new CustomerDto(null, "emily2", null);
        webTestClient.put()
                .uri("/customers/{id}", 7)
                .bodyValue(missingUpdateEmail)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Valid email is required");

        var invalidUpdateEmail = new CustomerDto(null, "emily2", "emily2");
        webTestClient.put()
                .uri("/customers/{id}", 7)
                .bodyValue(invalidUpdateEmail)
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Valid email is required");
    }
}
