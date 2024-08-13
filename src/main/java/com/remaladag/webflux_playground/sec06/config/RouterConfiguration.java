package com.remaladag.webflux_playground.sec06.config;

import com.remaladag.webflux_playground.sec06.exceptions.CustomerNotFoundException;
import com.remaladag.webflux_playground.sec06.exceptions.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Configuration
public class RouterConfiguration {

    @Autowired
    private CustomerRequestHandler handler;

    @Autowired
    private ApplicationExceptionHandler exceptionHandler;

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        return RouterFunctions.route()
                .GET("/customers", this.handler::getAllCustomers)
                .GET("/customers/paginated", this.handler::paginatedCustomers)
                .GET("/customers/{id}", this.handler::getCustomer)
                .POST("customers", this.handler::saveCustomer)
                .PUT("customers/{id}", this.handler::updateCustomer)
                .DELETE("customers/{id}", this.handler::deleteCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .build();
    }
  /*  @Bean
    public RouterFunction<ServerResponse> customerRoutes1() {
        return RouterFunctions.route()
                .path("customers", this::customerRoutes2) // first go to customers path
            //    .GET("/customers", this.handler::getAllCustomers)
            //    .GET("/customers/paginated", this.handler::paginatedCustomers)
            //    .GET("/customers/{id}", this.handler::getCustomer)
                .POST("customers", this.handler::saveCustomer)
                .PUT("customers/{id}", this.handler::updateCustomer)
                .DELETE("customers/{id}", this.handler::deleteCustomer)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .onError(InvalidInputException.class, exceptionHandler::handleException)
                .filter((request, next) -> {
                    System.out.println("Before handler invocation: " + request.path());
                    return ServerResponse.badRequest().build();
                })
                .build();
    }
    //@Bean
    private RouterFunction<ServerResponse> customerRoutes2() {
        return RouterFunctions.route()
                .GET("/paginated", this.handler::paginatedCustomers)
                .GET("/{id}", this.handler::getCustomer)
                .GET(this.handler::getAllCustomers)
                .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
                .build();
    }  */
}
