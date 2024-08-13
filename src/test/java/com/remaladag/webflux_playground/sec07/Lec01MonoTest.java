package com.remaladag.webflux_playground.sec07;

import com.remaladag.webflux_playground.sec07.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

public class Lec01MonoTest extends AbstractWebClient {

    private final WebClient webClient = createWebClient();

    @Test
    public void simpleGet() throws InterruptedException {
        this.webClient.get()
                .uri("/lec01/product/102")
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(print())
                .subscribe();

        Thread.sleep(Duration.ofSeconds(2).toMillis());
    }

    @Test
    public void concurrentRequests() throws InterruptedException {
        for (int i = 0; i <= 102; i++) {
            this.webClient.get()
                    .uri("/lec01/product/{id}", i)
                    .retrieve()
                    .bodyToMono(Product.class)
                    .doOnNext(print())
                    .subscribe();
        }

        Thread.sleep(Duration.ofSeconds(2).toMillis());
    }

}
