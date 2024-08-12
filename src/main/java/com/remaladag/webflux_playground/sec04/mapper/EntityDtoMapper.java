package com.remaladag.webflux_playground.sec04.mapper;

import com.remaladag.webflux_playground.sec04.dto.CustomerDto;
import com.remaladag.webflux_playground.sec04.entity.Customer;

public class EntityDtoMapper {

    public static Customer toEntity(CustomerDto dto) {
        return Customer.builder()
                .id(dto.id())
                .name(dto.name())
                .email(dto.email())
                .build();
    }

    public static CustomerDto toDto(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );
    }
}
