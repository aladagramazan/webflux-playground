package com.remaladag.webflux_playground.sec06.config;

import com.remaladag.webflux_playground.sec06.dto.CustomerDto;
import com.remaladag.webflux_playground.sec06.exceptions.ApplicationExceptions;
import com.remaladag.webflux_playground.sec06.service.CustomerService;
import com.remaladag.webflux_playground.sec06.validator.RequestValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
public class CustomerRequestHandler {

    @Autowired
    private CustomerService customerService;

    public Mono<ServerResponse> getAllCustomers(ServerRequest request) {
        // request.pathVariable("id");
        // request.queryParam("name");
        // request.headers();
        return customerService.getAllCustomers()
                .as(flux -> ServerResponse.ok().body(flux, CustomerDto.class));
    }

    public Mono<ServerResponse> paginatedCustomers(ServerRequest request) {
        var page = request.queryParam("page").map(Integer::parseInt).orElse(1);
        var size = request.queryParam("size").map(Integer::parseInt).orElse(3);

        return customerService.getAllCustomers(page, size)
                .collectList()  // infinite flux to finite list
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> getCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request) {
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(customerService::saveCustomer)
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return request.bodyToMono(CustomerDto.class)
                .transform(RequestValidator.validate())
                .as(validReq -> customerService.updateCustomer(id, validReq))
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
        var id = Integer.parseInt(request.pathVariable("id"));
        return customerService.deleteCustomer(id)
                .filter(b -> b)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .then(ServerResponse.ok().build());
    }
}
