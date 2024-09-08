package com.pricingservice.application;

import com.pricingservice.domain.Price;
import com.pricingservice.infrastucture.repository.PriceRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class PriceServiceImplTest {

    @Mock
    private PriceRepository priceRepository;

    @Mock
    private Counter counter;

    private SimpleMeterRegistry realMeterRegistry;
    private PriceServiceImpl priceService;
    private MeterRegistry meterRegistry;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        meterRegistry = mock(MeterRegistry.class);
        realMeterRegistry = new SimpleMeterRegistry();
    }

    private void useMockMeterRegistry() {
        when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(counter);
        priceService = new PriceServiceImpl(priceRepository, meterRegistry);
    }

    private void useRealMeterRegistry() {
        priceService = new PriceServiceImpl(priceRepository, realMeterRegistry);
    }

    @Test
    void testGetPriceAt10On14th() {
        // Mocked MeterRegistry
        useMockMeterRegistry();

        // given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        List<Price> prices = Arrays.asList(
                new Price(brandId, productId, 1, applicationDate.minusHours(10), applicationDate.plusDays(1),
                        BigDecimal.valueOf(35.50), "EUR", 0)
        );

        // when
        when(priceRepository.findByProductIdAndBrandId(productId, brandId)).thenReturn(prices);

        Price price = priceService.getPrice(applicationDate, productId, brandId);

        // then
        assertNotNull(price);
        assertEquals(1, price.getPriceList());
        assertEquals(BigDecimal.valueOf(35.50), price.getPrice());

        // No fallback was called
        verify(counter, never()).increment();
    }

    @Test
    void testFallbackGetPrice() {
        // Real MeterRegistry
        useRealMeterRegistry();

        // given
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = 35455L;
        Long brandId = 1L;

        // when
        Price price = priceService.fallbackGetPrice(applicationDate, productId, brandId, new RuntimeException("Database error"));

        // then
        assertNotNull(price);
        assertEquals(BigDecimal.ZERO, price.getPrice());

        // Fallback was called
        Counter fallbackCounter = realMeterRegistry.counter("price.service.fallback.calls",
                "exception", "RuntimeException",
                "method", "getPrice");

        assertNotNull(fallbackCounter);
        assertEquals(1.0, fallbackCounter.count());
    }
}
