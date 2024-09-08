package com.pricingservice.application;

import com.pricingservice.domain.Price;

import java.time.LocalDateTime;

public interface PriceService {
    Price getPrice(LocalDateTime applicationDate, Long productId, Long brandId);
}
