package com.remaladag.webflux_playground.sec07.service;

import com.remaladag.webflux_playground.sec07.dto.ProductDto;
import com.remaladag.webflux_playground.sec07.mapper.EntityDtoMapper;
import com.remaladag.webflux_playground.sec07.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Flux<ProductDto> saveProducts(Flux<ProductDto> flux) {
        return flux.map(EntityDtoMapper::toEntity)
                .as(productRepository::saveAll)
                .map(EntityDtoMapper::toDto);
    }

    // get products count
    public Mono<Long> getProductsCount() {
        return productRepository.count();
    }

    public Flux<ProductDto> getAllProducts() {
        return productRepository.findAll()
                .map(EntityDtoMapper::toDto);
    }
}
