package com.remaladag.webflux_playground.sec02.repository;

import com.remaladag.webflux_playground.sec02.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer>{

    Flux<Product> findByPriceBetween(int from, int to);

    Flux<Product> findBy(Pageable pageable); // findBy and findAllBy same.
}
