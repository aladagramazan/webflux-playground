package com.remaladag.webflux_playground.sec10.dto;

public record CalculatorResponse(Integer first,
                                 Integer second,
                                 String operation,
                                 Double result) {

}
