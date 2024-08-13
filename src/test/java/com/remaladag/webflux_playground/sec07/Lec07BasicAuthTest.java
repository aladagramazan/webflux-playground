package com.remaladag.webflux_playground.sec07;

import com.remaladag.webflux_playground.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Lec07BasicAuthTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(Lec07BasicAuthTest.class);

    private final WebClient webClient = createWebClient(builder -> builder
            .defaultHeaders(h -> h.setBasicAuth("java", "secret")));

    @Test
    public void basicAuthTest() {
        this.webClient.get()
                .uri("/lec07/product/{id}", 1)
                //  .headers(h -> h.setBasicAuth("java", "secret"))
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }
}
