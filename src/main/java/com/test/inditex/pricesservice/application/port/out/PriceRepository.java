package com.test.inditex.pricesservice.application.port.out;

import com.test.inditex.pricesservice.domain.model.BrandId;
import com.test.inditex.pricesservice.domain.model.Price;
import com.test.inditex.pricesservice.domain.model.ProductId;

import java.time.LocalDateTime;
import java.util.List;

public interface PriceRepository {
    List<Price> findCandidates(LocalDateTime applicationDate, ProductId productId, BrandId brandId);
}
