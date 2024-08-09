package com.remaladag.webflux_playground.sec02;

import com.remaladag.webflux_playground.sec02.dto.OrderDetails;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Lec04DatabaseClientTest extends AbstractTest {

    private static final Logger log = LoggerFactory.getLogger(Lec04DatabaseClientTest.class);

    @Autowired
    private DatabaseClient client;

    @Test
    public void getOrderDetailsByProduct() {
        var query = """
                     SELECT
                    co.order_id,
                    c.name AS customer_name,
                    p.description AS product_name,
                    co.amount,
                    co.order_date
                FROM
                    customer2 c
                        INNER JOIN customer_order co ON c.id = co.customer_id
                        INNER JOIN product p ON p.id = co.product_id
                WHERE
                        p.description = :description
                ORDER BY co.amount DESC
                    """;
        this.client.sql(query)
                .bind("description", "iphone 18")
                .mapProperties(OrderDetails.class)
                .all()
                .doOnNext(co -> log.info("order details: {}", co))
                .as(StepVerifier::create)
                .assertNext(dto -> assertEquals(850, dto.amount()))
                .assertNext(dto -> assertEquals(775, dto.amount()))
                .assertNext(dto -> assertEquals(750, dto.amount()))
                .expectComplete()
                .verify();
    }
}
