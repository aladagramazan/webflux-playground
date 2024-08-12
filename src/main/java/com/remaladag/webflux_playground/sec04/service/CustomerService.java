package com.remaladag.webflux_playground.sec04.service;

import com.remaladag.webflux_playground.sec04.dto.CustomerDto;
import com.remaladag.webflux_playground.sec04.mapper.EntityDtoMapper;
import com.remaladag.webflux_playground.sec04.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Flux<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                .map(EntityDtoMapper::toDto);
    }

    public Flux<CustomerDto> getAllCustomers(Integer page, Integer size) {
        return customerRepository.findBy(PageRequest.of(page - 1, size))
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> getCustomerById(Integer id) {
        return customerRepository.findById(id)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> mono) {
        return mono.map(EntityDtoMapper::toEntity)
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<CustomerDto> updateCustomer(Integer id, Mono<CustomerDto> mono) {
        return customerRepository.findById(id)
                .flatMap(entity -> mono)
                .map(EntityDtoMapper::toEntity)
                .doOnNext(c -> c.setId(id))
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::toDto);
    }

    public Mono<Boolean> deleteCustomer(Integer id) {
        return customerRepository.deleteCustomerById(id);
    }
}
