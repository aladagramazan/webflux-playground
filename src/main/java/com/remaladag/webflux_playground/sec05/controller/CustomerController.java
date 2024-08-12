package com.remaladag.webflux_playground.sec05.controller;

import com.remaladag.webflux_playground.sec05.dto.CustomerDto;
import com.remaladag.webflux_playground.sec05.exceptions.ApplicationExceptions;
import com.remaladag.webflux_playground.sec05.filter.Category;
import com.remaladag.webflux_playground.sec05.service.CustomerService;
import com.remaladag.webflux_playground.sec05.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    public Flux<CustomerDto> getAllCustomers(@RequestAttribute("category") Category category) {
        System.out.println("Category: " + category);
        return customerService.getAllCustomers();
    }

    @GetMapping("paginated")
    public Mono<List<CustomerDto>> getAllCustomers(@RequestParam(defaultValue = "1") Integer page,
                                                   @RequestParam(defaultValue = "3") Integer size) {
        return customerService.getAllCustomers(page, size)
                .collectList();
    }

    @GetMapping("{id}")
    public Mono<CustomerDto> getCustomerById(@PathVariable Integer id) {
        return customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @PostMapping
    public Mono<CustomerDto> saveCustomer(@RequestBody Mono<CustomerDto> mono) {
       return mono.transform(RequestValidator.validate())
               .as(customerService::saveCustomer);
    }

    @PutMapping("{id}")
    public Mono<CustomerDto> updateCustomer(@PathVariable Integer id, @RequestBody Mono<CustomerDto> mono) {
        return mono.transform(RequestValidator.validate())
                .as(validReq -> customerService.updateCustomer(id, validReq))
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteCustomer(@PathVariable Integer id) {
        return customerService.deleteCustomer(id)
                .filter(b -> b)
                .map(b -> ResponseEntity.ok().<Void>build())
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .then();
    }
}
