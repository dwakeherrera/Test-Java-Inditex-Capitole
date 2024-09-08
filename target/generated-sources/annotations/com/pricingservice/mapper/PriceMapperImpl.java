package com.pricingservice.mapper;

import com.pricingservice.domain.Price;
import com.pricingservice.dto.PriceDto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-09-06T16:59:15+0200",
    comments = "version: 1.5.2.Final, compiler: javac, environment: Java 17.0.6 (Oracle Corporation)"
)
@Component
public class PriceMapperImpl implements PriceMapper {

    @Override
    public PriceDto toDto(Price price) {
        if ( price == null ) {
            return null;
        }

        Long brandId = null;
        Long productId = null;
        int priceList = 0;
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;
        BigDecimal price1 = null;
        String currency = null;
        int priority = 0;

        brandId = price.getBrandId();
        productId = price.getProductId();
        priceList = price.getPriceList();
        startDate = price.getStartDate();
        endDate = price.getEndDate();
        price1 = price.getPrice();
        currency = price.getCurrency();
        priority = price.getPriority();

        PriceDto priceDto = new PriceDto( brandId, productId, priceList, startDate, endDate, price1, currency, priority );

        return priceDto;
    }

    @Override
    public Price toEntity(PriceDto priceDto) {
        if ( priceDto == null ) {
            return null;
        }

        Price price = new Price();

        price.setBrandId( priceDto.brandId() );
        price.setProductId( priceDto.productId() );
        price.setPriceList( priceDto.priceList() );
        price.setStartDate( priceDto.startDate() );
        price.setEndDate( priceDto.endDate() );
        price.setPrice( priceDto.price() );
        price.setCurrency( priceDto.currency() );
        price.setPriority( priceDto.priority() );

        return price;
    }
}
