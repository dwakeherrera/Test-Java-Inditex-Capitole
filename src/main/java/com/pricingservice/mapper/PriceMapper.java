package com.pricingservice.mapper;

import com.pricingservice.domain.Price;
import com.pricingservice.dto.PriceDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {
    PriceDto toDto(Price price);
    Price toEntity(PriceDto priceDto);
}
