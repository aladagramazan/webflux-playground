package com.rem.aggregator_service.dto;

import com.rem.aggregator_service.domain.Ticker;

import java.time.LocalDateTime;

public record StockPriceResponse(Ticker ticker,
                                 Integer price) {
}
