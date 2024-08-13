package com.remaladag.webflux_playground.sec06.config;

import com.remaladag.webflux_playground.sec06.dto.HandleExceptionDTO;
import com.remaladag.webflux_playground.sec06.exceptions.CustomerNotFoundException;
import com.remaladag.webflux_playground.sec06.exceptions.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Service
public class ApplicationExceptionHandler {

    public Mono<ServerResponse> handleException(CustomerNotFoundException ex, ServerRequest request) {
        HandleExceptionDTO dto = HandleExceptionDTO.builder()
                .status(HttpStatus.NOT_FOUND)
                .exception(ex)
                .request(request)
                .consumer(problem -> {
                    problem.setType(URI.create("https://example.com/problems/customer-not-found"));
                    problem.setTitle("Customer not found");
                })
                .build();
        return handleException(dto);
    }

    public Mono<ServerResponse> handleException(InvalidInputException ex, ServerRequest request) {
        HandleExceptionDTO dto = HandleExceptionDTO.builder()
                .status(HttpStatus.BAD_REQUEST)
                .exception(ex)
                .request(request)
                .consumer(problem -> {
                    problem.setType(URI.create("https://example.com/problems/invalid-input"));
                    problem.setTitle("Invalid input");
                })
                .build();
        return handleException(dto);
    }

    public Mono<ServerResponse> handleException(HandleExceptionDTO dto) {
        String message = dto.getException().getMessage();
        HttpStatus status = dto.getStatus();
        var problem = ProblemDetail.forStatusAndDetail(status, message);
        dto.getConsumer().accept(problem);
        problem.setInstance(URI.create(dto.getRequest().path()));
        return ServerResponse.status(status)
                .bodyValue(problem);
    }
}
