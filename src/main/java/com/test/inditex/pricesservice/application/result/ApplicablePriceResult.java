package com.test.inditex.pricesservice.application.result;

import com.test.inditex.pricesservice.domain.model.*;

public record ApplicablePriceResult(
        ProductId productId,
        BrandId brandId,
        PriceList priceList,
        DateRange validity,
        Money money
) {
}
