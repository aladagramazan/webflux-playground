package com.remaladag.webflux_playground.sec10;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

public abstract class AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebClient.class);

    protected <T> Consumer<T> print() {
        return t -> log.info("received: {}", t);
    }

    protected WebClient createWebClient() {
        return createWebClient(builder -> {});
    }

    protected WebClient createWebClient(Consumer<WebClient.Builder> consumer) {
        var builder =  WebClient.builder()
                .baseUrl("http://localhost:7070/demo03");
        consumer.accept(builder);
        return builder.build();
    }
}
