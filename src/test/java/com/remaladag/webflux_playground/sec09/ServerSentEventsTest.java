package com.remaladag.webflux_playground.sec09;

import com.remaladag.webflux_playground.sec08.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureWebTestClient
@SpringBootTest(properties = {
        "sec=sec09"
        // "logging.level.org.springframework.r2dbc=DEBUG"
})
public class ServerSentEventsTest {

    private static final Logger log = LoggerFactory.getLogger(ServerSentEventsTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void serverSentEventsTest() {
        this.client.get()
                .uri("/products/stream/80")
                .accept(MediaType.TEXT_EVENT_STREAM)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(ProductDto.class)
                .getResponseBody()
                .take(3)
                .doOnNext(p -> log.info("received: {}", p))
                .collectList() // take 3
                .as(StepVerifier::create)
                .assertNext(list -> {
                    assertEquals(3, list.size());
                    assertTrue(list.stream().allMatch(p -> p.price() <= 80));
                })
                .expectComplete()
                .verify();
    }
}
