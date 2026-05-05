package com.test.inditex.pricesservice.domain.model;

import com.test.inditex.pricesservice.domain.error.NoApplicablePriceException;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class ApplicablePrices {

    private final List<Price> prices;

    public ApplicablePrices(List<Price> prices) {
        this.prices = List.copyOf(Objects.requireNonNull(prices));
    }

    public Price highestPriority() {
        return prices.stream()
                .max(Comparator.comparing(Price::priority))
                .orElseThrow(NoApplicablePriceException::new);
    }

}
