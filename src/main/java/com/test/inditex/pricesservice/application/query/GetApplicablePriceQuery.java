package com.test.inditex.pricesservice.application.query;

import com.test.inditex.pricesservice.domain.model.BrandId;
import com.test.inditex.pricesservice.domain.model.ProductId;

import java.time.LocalDateTime;
import java.util.Objects;

public record GetApplicablePriceQuery(
        LocalDateTime applicationDate,
        ProductId productId,
        BrandId brandId
) {
    public GetApplicablePriceQuery {
        Objects.requireNonNull(applicationDate);
        Objects.requireNonNull(productId);
        Objects.requireNonNull(brandId);
    }
}
