package com.test.inditex.pricesservice.infrastructure.adapter.in.web;

import com.test.inditex.pricesservice.application.query.GetApplicablePriceQuery;
import com.test.inditex.pricesservice.application.result.ApplicablePriceResult;
import com.test.inditex.pricesservice.domain.model.BrandId;
import com.test.inditex.pricesservice.domain.model.ProductId;

import java.time.LocalDateTime;

public class PriceRestMapper {

    public GetApplicablePriceQuery toQuery(
            LocalDateTime applicationDate,
            Long productId,
            Long brandId
    ) {
        return new GetApplicablePriceQuery(
                applicationDate,
                ProductId.of(productId),
                BrandId.of(brandId)
        );
    }

    public PriceResponse toResponse(ApplicablePriceResult result) {
        return new PriceResponse(
                result.productId().value(),
                result.brandId().value(),
                result.priceList().value(),
                result.validity().startDate(),
                result.validity().endDate(),
                result.money().amount(),
                result.money().currency().getCurrencyCode()
        );
    }

}
