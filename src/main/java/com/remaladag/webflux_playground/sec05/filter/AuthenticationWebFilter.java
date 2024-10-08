package com.remaladag.webflux_playground.sec05.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

@Order(1) // first filter
@Service
public class AuthenticationWebFilter implements WebFilter {

    @Autowired
    private FilterErrorHandler errorHandler;

    private static final Map<String, Category> TOKEN_CATEGORY_MAP = Map.of(
            "secret321", Category.STANDARD,
            "secret654", Category.PRIME
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var token = exchange.getRequest().getHeaders().getFirst("auth-token");
        if (Objects.nonNull(token) && TOKEN_CATEGORY_MAP.containsKey(token)) {
            exchange.getAttributes().put("category", TOKEN_CATEGORY_MAP.get(token));
            return chain.filter(exchange);
        }
        //  return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
        return errorHandler.sendProblemDetails(exchange, HttpStatus.UNAUTHORIZED, "Invalid token");
    }
}
