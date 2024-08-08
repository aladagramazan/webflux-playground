package com.remaladag.webflux_playground.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/traditional")
public class TraditionalController {

    private static final Logger log = LoggerFactory.getLogger(TraditionalController.class);
    private final RestClient client = RestClient.builder()
            .baseUrl("http://localhost:7070")
            .build();

    @GetMapping("/products")
    public List<Product> getProducts() {
        log.info("getProducts");
        var productList = client.get()
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });
        log.info("received response: {}", productList);
        return productList;
    }

    @GetMapping("/products2")
    public Flux<Product> getProducts2() {  // this is not reactive programming
        log.info("getProducts2");
        var productList = client.get() // we are doing here is we are still writing synchronous blocking style code
                .uri("/demo01/products")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });
        log.info("received response: {}", productList);
        return Flux.fromIterable(productList); // this is not reactive programming

        // compiler happy but this is not reactive programming
    }

    @GetMapping("/products3")
    public Flux<Product> getProducts3() {
        log.info("getProducts3");
        var productList = client.get()
                .uri("/demo01/products/notorious")
                .retrieve()
                .body(new ParameterizedTypeReference<List<Product>>() {
                });
        log.info("received response: {}", productList);
        return Flux.fromIterable(productList);
    }
}
