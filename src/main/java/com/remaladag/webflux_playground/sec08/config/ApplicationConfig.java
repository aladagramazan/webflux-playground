package com.remaladag.webflux_playground.sec08.config;

import com.remaladag.webflux_playground.sec08.dto.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class ApplicationConfig {

    @Bean
    public Sinks.Many<ProductDto> sink() {
        return Sinks.many().replay().limit(1); // last emitted item
    }
}
