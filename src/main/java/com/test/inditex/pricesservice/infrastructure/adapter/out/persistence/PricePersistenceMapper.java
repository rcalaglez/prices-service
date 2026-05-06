package com.test.inditex.pricesservice.infrastructure.adapter.out.persistence;

import com.test.inditex.pricesservice.domain.model.*;

public class PricePersistenceMapper {

    public Price toDomain(PriceEntity entity) {
        return Price.create(
                BrandId.of(entity.getBrandId()),
                ProductId.of(entity.getProductId()),
                PriceList.of(entity.getPriceList()),
                DateRange.between(entity.getStartDate(), entity.getEndDate()),
                Priority.of(entity.getPriority()),
                Money.of(entity.getPrice(), entity.getCurrency())
        );
    }

}
