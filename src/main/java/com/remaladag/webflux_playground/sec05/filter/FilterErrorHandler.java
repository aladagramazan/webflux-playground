package com.remaladag.webflux_playground.sec05.filter;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class FilterErrorHandler {

    @Autowired
    private ServerCodecConfigurer serverCodecConfigurer;

    private ServerResponse.Context context;

    @PostConstruct
    public void init() {
        this.context = new ContextImpl(serverCodecConfigurer);
    }

    public Mono<Void> sendProblemDetails(ServerWebExchange exchange, HttpStatus status, String message) {
        var problem = ProblemDetail.forStatusAndDetail(status, message);
        return ServerResponse.status(status)
                .bodyValue(problem)
                .flatMap(response -> response.writeTo(exchange, this.context));
    }

    private record ContextImpl(ServerCodecConfigurer serverCodecConfigurer) implements ServerResponse.Context {

        @Override
        public List<HttpMessageWriter<?>> messageWriters() {
            return serverCodecConfigurer.getWriters();
        }

        @Override
        public List<ViewResolver> viewResolvers() {
            return List.of();
        }
    }
}
