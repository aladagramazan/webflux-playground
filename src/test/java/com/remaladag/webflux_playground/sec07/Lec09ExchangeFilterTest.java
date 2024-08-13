package com.remaladag.webflux_playground.sec07;

import com.remaladag.webflux_playground.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.UUID;

public class Lec09ExchangeFilterTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(Lec09ExchangeFilterTest.class);

    private final WebClient webClient = createWebClient(builder -> builder.filter(tokenGenerator())
            .filter(requestLogger()));

    @Test
    public void exchangeFilterTest() {
        for (int i = 1; i <= 5; i++) {
            this.webClient.get()
                    .uri("/lec09/product/{id}", i)
                    //  .headers(h -> h.setBasicAuth("java", "secret"))
                    .attribute("enable-logging", i % 2 == 1)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .then()
                    .as(StepVerifier::create)
                    .expectComplete()
                    .verify();
        }
    }

    private ExchangeFilterFunction tokenGenerator() {
        return (clientRequest, next) -> {
            log.info("I am in the filter function");
            var token = UUID.randomUUID().toString().replace("-", "");
            log.info("Generated Token: {}", token);
            var modifiedRequest = ClientRequest.from(clientRequest)
                    .headers(h -> h.setBearerAuth(token))
                    .build();
            return next.exchange(modifiedRequest);
        };
    }

    private ExchangeFilterFunction requestLogger() {
        return (clientRequest, next) -> {
            var isEnabled = (Boolean) clientRequest.attributes().getOrDefault("enable-logging", false);
            if (isEnabled) {
                log.info("Request: {}: {}", clientRequest.method(), clientRequest.url());
            }

            return next.exchange(clientRequest);
        };
    }
}
