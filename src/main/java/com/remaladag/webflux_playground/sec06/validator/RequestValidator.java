package com.remaladag.webflux_playground.sec06.validator;

import com.remaladag.webflux_playground.sec06.dto.CustomerDto;
import com.remaladag.webflux_playground.sec06.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDto>> validate() {
        return mono -> mono
                .filter(hasName())
                .switchIfEmpty(ApplicationExceptions.missingName())
                .filter(hasEmail())
                .switchIfEmpty(ApplicationExceptions.missingEmail());
    }


    private static Predicate<CustomerDto> hasName() {
        return dto -> Objects.nonNull(dto.name()) && !dto.name().isBlank();
    }

    private static Predicate<CustomerDto> hasEmail() {
        return dto -> Objects.nonNull(dto.email()) && !dto.email().isBlank() && dto.email().contains("@");
    }
}
