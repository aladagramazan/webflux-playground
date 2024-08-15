package com.remaladag.webflux_playground.sec08;

import com.remaladag.webflux_playground.sec07.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.nio.file.Path;

public class ProductsUploadDownloadTest {

    private static final Logger log = LoggerFactory.getLogger(ProductsUploadDownloadTest.class);
    private final ProductClient client = new ProductClient();

    @Test
    public void uploadProducts() {
        var flux = Flux.range(1, 1_000_000)
                .map(i -> new ProductDto(null, "product-" + i, i));

        client.uploadProducts(flux)
                .doOnNext(p -> log.info("received: {}", p))
                .then()
                .as(StepVerifier::create)
                .expectComplete()
                .verify();
    }

    @Test
    public void downloadProducts() {
        client.downloadProducts()
                .map(ProductDto::toString)
                .as(flux -> FileWriter.create(flux, Path.of("products.txt")))
                .as(StepVerifier::create)
                .expectComplete()
                .verify();

    }
}
