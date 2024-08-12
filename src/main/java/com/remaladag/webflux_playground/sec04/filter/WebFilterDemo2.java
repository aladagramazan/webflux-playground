package com.remaladag.webflux_playground.sec04.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Service
@Order(2) // This is the order of the filter. This filter is second filter to be executed
public class WebFilterDemo2 implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(WebFilterDemo2.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("received request 2");
      //  return Mono.empty();  // I am not ok, I will not let you go to controller
        return chain.filter(exchange); // I am ok, you can go to controller
    }
}
