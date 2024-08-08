package com.remaladag.webflux_playground.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/reactive")
public class ReactiveController {

    private static final Logger log = LoggerFactory.getLogger(ReactiveController.class);
    private static WebClient client = WebClient.builder()
            .baseUrl("http://localhost:7070")
            .build();

    @GetMapping("/products")
    public Flux<Product> getProducts() {
        log.info("getProducts");
        return client.get()
                .uri("/demo01/products")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(product -> log.info("received product: {}", product));
    }

    @GetMapping(value = "/products/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Product> getProductsStream() {
        log.info("getProducts");
        return client.get()
                .uri("/demo01/products")
                .retrieve()
                .bodyToFlux(Product.class)
                .doOnNext(product -> log.info("received product: {}", product));
    }

    @GetMapping("/products2")
    public Flux<Product> getProducts2() {
        log.info("getProducts");
        return client.get()
                .uri("/demo01/products/notorious")
                .retrieve()
                .bodyToFlux(Product.class)
                .onErrorComplete()  // until we get error, we will keep on emitting the data
                .doOnNext(product -> log.info("received product: {}", product));

        // there is not error handling here, so if there is an error, the stream will be closed

        // we can it more resilient.
        // some external service fail, our service should not fail
    }

}
