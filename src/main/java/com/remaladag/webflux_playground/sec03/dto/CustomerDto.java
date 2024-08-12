package com.remaladag.webflux_playground.sec03.dto;

import jdk.jshell.Snippet;

public record CustomerDto(Integer id,
                          String name,
                          String email) {
}
