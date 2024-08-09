package com.remaladag.webflux_playground.sec02.entity;

import lombok.*;
import org.springframework.data.annotation.Id;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Product {

    @Id
    private Integer id;
    private String description;
    private Integer price;
}
