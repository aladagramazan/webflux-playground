package com.remaladag.webflux_playground.sec02;

import com.remaladag.webflux_playground.sec02.repository.CustomerOrderRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Lec03CustomerOrderRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec03CustomerOrderRepositoryTest.class);

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Test
    public void getProductsOrderedByCustomer() {
        this.customerOrderRepository.getProductsOrderOrderedByCustomer("mike")
                .doOnNext(p -> log.info("product: {}", p))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .expectComplete()
                .verify();
    }

    @Test
    public void getOrderDetailsByProduct() {
        this.customerOrderRepository.getOrderDetailsByProduct("iphone 18")
                .doOnNext(co -> log.info("order details: {}", co))
                .as(StepVerifier::create)
                .assertNext(dto -> assertEquals(850, dto.amount()))
                .assertNext(dto -> assertEquals(775, dto.amount()))
                .assertNext(dto -> assertEquals(750, dto.amount()))
                .expectComplete()
                .verify();
    }
}
