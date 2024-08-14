package com.remaladag.webflux_playground.sec07.repository;

import com.remaladag.webflux_playground.sec07.entity.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

}
