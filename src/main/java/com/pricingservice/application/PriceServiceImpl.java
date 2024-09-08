package com.pricingservice.application;

import com.pricingservice.domain.Price;
import com.pricingservice.infrastucture.repository.PriceRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class PriceServiceImpl implements PriceService {

    private final PriceRepository priceRepository;
    private final MeterRegistry meterRegistry;

    public PriceServiceImpl(PriceRepository priceRepository, MeterRegistry meterRegistry) {
        this.priceRepository = priceRepository;
        this.meterRegistry = meterRegistry;
    }

    @Override
    @CircuitBreaker(name = "priceService", fallbackMethod = "fallbackGetPrice")
    public Price getPrice(LocalDateTime applicationDate, Long productId, Long brandId) {
        // Get all availables prices for given product and brand
        List<Price> prices = priceRepository.findByProductIdAndBrandId(productId, brandId);
        return findValidPrice(prices, applicationDate);
    }

    private Price findValidPrice(List<Price> prices, LocalDateTime applicationDate) {
        // Filter active prices for given date
        // Price with better priority - null if no valid price
        return prices.stream()
                .filter(price -> isWithinDateRange(price, applicationDate))
                .max(Comparator.comparingInt(Price::getPriority))
                .orElse(null);
    }

    private boolean isWithinDateRange(Price price, LocalDateTime applicationDate) {
        return (applicationDate.isAfter(price.getStartDate()) || applicationDate.isEqual(price.getStartDate())) &&
                (applicationDate.isBefore(price.getEndDate()) || applicationDate.isEqual(price.getEndDate()));
    }

    // Fallback method in case of failure
    public Price fallbackGetPrice(LocalDateTime applicationDate, Long productId, Long brandId, Throwable t) {
        // Counter with proper tagging for possible failures
        meterRegistry.counter("price.service.fallback.calls",
                "exception", t.getClass().getSimpleName(),
                "method", "getPrice").increment();

        log.warn("Fallback method triggered for getPrice. ApplicationDate: {}, ProductId: {}, BrandId: {}. Exception: {}",
                applicationDate, productId, brandId, t.getMessage());

        return new Price(brandId, productId, 0, applicationDate, applicationDate.plusDays(1),
                BigDecimal.ZERO, "EUR", 0);
    }
}
