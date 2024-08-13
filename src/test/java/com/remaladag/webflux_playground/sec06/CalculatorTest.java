package com.remaladag.webflux_playground.sec06;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@AutoConfigureWebTestClient
@SpringBootTest(properties = {
        "sec=sec06"
        // "logging.level.org.springframework.r2dbc=DEBUG"
})
public class CalculatorTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void calculatorTest() {
        // success
        validate(CalculatorDto.builder().a(10).b(5).operation("+").statusCode(200).expected("15").build());
        validate(CalculatorDto.builder().a(10).b(5).operation("-").statusCode(200).expected("5").build());
        validate(CalculatorDto.builder().a(10).b(5).operation("*").statusCode(200).expected("50").build());
        validate(CalculatorDto.builder().a(10).b(5).operation("/").statusCode(200).expected("2").build());

        // bad request
        validate(CalculatorDto.builder().a(10).b(0).operation("/").statusCode(400).expected("b cannot be 0").build());
        validate(CalculatorDto.builder().a(10).b(5).operation("x").statusCode(400).expected("operation header should be one of +, -, *, /").build());
        validate(CalculatorDto.builder().a(10).b(5).statusCode(400).expected("operation header should be one of +, -, *, /").build());

    }


    private void validate(CalculatorDto dto) {
        // given
        // when
        // then
        this.webTestClient.get()
                .uri("/calculator/{a}/{b}", dto.getA(), dto.getB())
                .headers(h -> {
                    if (Objects.nonNull(dto.getOperation())) {
                        h.add("operation", dto.getOperation());
                    }

                })
                .exchange()
                .expectStatus().isEqualTo(dto.getStatusCode())
                .expectBody(String.class)
                .value(s -> {
                    assertNotNull(s);
                    assertEquals(dto.getExpected(), s);
                });
    }

    @Data
    @Builder
    private static class CalculatorDto {
        private int a;
        private int b;
        private String operation;
        private int statusCode;
        private String expected;
    }
}
