package com.remaladag.webflux_playground.sec02;

import com.remaladag.webflux_playground.sec02.entity.Customer;
import com.remaladag.webflux_playground.sec02.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Lec01CustomerRepositoryTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec01CustomerRepositoryTest.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findAll() {
        this.customerRepository.findAll()
                .doOnNext(c -> log.info("customer: {}", c))
                .as(StepVerifier::create)
                .expectNextCount(20)
                .expectComplete()
                .verify();
    }

    @Test
    public void findById() {
        this.customerRepository.findById(3)
                .doOnNext(c -> log.info("customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(c -> assertEquals("jake", c.getName()))
                .expectComplete()
                .verify();
    }

    @Test
    public void findByName() {
        this.customerRepository.findByName("jake")
                .doOnNext(c -> log.info("customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(c -> assertEquals("jake@gmail.com", c.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    public void findByEmailEndingWith() {
        this.customerRepository.findByEmailEndingWith("ke@gmail.com")
                .doOnNext(c -> log.info("customer: {}", c))
                .as(StepVerifier::create)
                .expectNextCount(1)
                .assertNext(c -> assertEquals("jake@gmail.com", c.getEmail()))
                //.assertNext(c -> assertEquals("mike@gmail.com", c.getEmail()))
                .expectComplete()
                .verify();
    }

    @Test
    public void insertAndDeleteCustomer() {
        var customer = Customer.builder()
                .name("rem3")
                .email("rem3.rem@gmail.com")
                .build();

        this.customerRepository.save(customer)
                .doOnNext(c -> log.info("customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(c -> assertNotNull(c.getId()))
                .expectComplete()
                .verify();

        //count
        this.customerRepository.count()
                .doOnNext(c -> log.info("count: {}", c))
                .as(StepVerifier::create)
                .expectNext(20L)
                .expectComplete()
                .verify();

        //delete
        this.customerRepository.deleteById(11)
                .then(this.customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(20L)
                .expectComplete()
                .verify();
    }

    @Test
    public void updateCustomer() {
        this.customerRepository.findByName("ava")
                .doOnNext(c-> c.setName("ava2"))
                .flatMap(this.customerRepository::save)
                .doOnNext(c -> log.info("customer: {}", c))
                .as(StepVerifier::create)
                .assertNext(c -> assertEquals("ava2", c.getName()))
                .expectComplete()
                .verify();
    }
}
