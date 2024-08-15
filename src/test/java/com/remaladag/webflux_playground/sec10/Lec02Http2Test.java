package com.remaladag.webflux_playground.sec10;

import com.remaladag.webflux_playground.sec10.dto.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Lec02Http2Test extends AbstractWebClient {

    private final WebClient client = createWebClient(b -> {
        var poolSize = 1;
        var provider = ConnectionProvider.builder("vins")
                .lifo()
                .maxConnections(poolSize)
                .pendingAcquireMaxCount(poolSize)
                .build();
        var httpClient = HttpClient.create(provider)
                .protocol(HttpProtocol.H2C) // I HAVE NOT CERTIFICATE BUT WHEN I USE HTTP2 I GET AN ERROR
                .compress(true)
                .keepAlive(true);
        b.clientConnector(new ReactorClientHttpConnector(httpClient));

        // HTTP2 HAVE ONE CONNECTION
    });

    @Test
    public void concurrentRequests() {
        var max = 20000;
        Flux.range(1, max)
                .flatMap(this::getProduct, max)
                .collectList()
                .as(StepVerifier::create)
                .assertNext(l -> assertEquals(max, l.size()))
                .expectComplete()
                .verify();
    }

    private Mono<Product> getProduct(int id) {
        return this.client.get()
                .uri("/product/{id}", id)
                .retrieve()
                .bodyToMono(Product.class);
    }
}
