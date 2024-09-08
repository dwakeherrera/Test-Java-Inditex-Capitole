package com.pricingservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PriceDto(
        Long brandId,
        Long productId,
        int priceList,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal price,
        String currency,
        int priority
) {}
