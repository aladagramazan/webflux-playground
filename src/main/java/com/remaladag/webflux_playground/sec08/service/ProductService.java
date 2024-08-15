package com.remaladag.webflux_playground.sec08.service;

import com.remaladag.webflux_playground.sec08.dto.ProductDto;
import com.remaladag.webflux_playground.sec08.mapper.EntityDtoMapper;
import com.remaladag.webflux_playground.sec08.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private Sinks.Many<ProductDto> sink;

    public Mono<ProductDto> saveProduct(Mono<ProductDto> mono) {
        return mono.map(EntityDtoMapper::toEntity)
                .flatMap(productRepository::save)
                .map(EntityDtoMapper::toDto)
                .doOnNext(sink::tryEmitNext);
    }

    public Flux<ProductDto> productStream() {
        return sink.asFlux();
    }
}
