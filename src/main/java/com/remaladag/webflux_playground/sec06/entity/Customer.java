package com.remaladag.webflux_playground.sec06.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

// we do not hava in @Entity r2dbc
//@Column,@Table

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("customer2")
public class Customer {

    @Id
    private Integer id;
    @Column("name")
    private String name;
    private String email;
}
