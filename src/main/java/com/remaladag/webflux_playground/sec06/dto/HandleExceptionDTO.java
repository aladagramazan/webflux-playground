package com.remaladag.webflux_playground.sec06.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.function.Consumer;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HandleExceptionDTO {
    private HttpStatus status;
    private Exception exception;
    private ServerRequest request;
    private Consumer<ProblemDetail> consumer;
}
