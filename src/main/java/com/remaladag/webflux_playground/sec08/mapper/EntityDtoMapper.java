package com.remaladag.webflux_playground.sec08.mapper;

import com.remaladag.webflux_playground.sec08.dto.ProductDto;
import com.remaladag.webflux_playground.sec08.entity.Product;

public class EntityDtoMapper {

    public static Product toEntity(ProductDto dto) {
        return Product.builder()
                .id(dto.id())
                .description(dto.description())
                .price(dto.price())
                .build();
    }

    public static ProductDto toDto(Product product) {
        return new ProductDto(
                product.getId(),
                product.getDescription(),
                product.getPrice()
        );
    }
}
