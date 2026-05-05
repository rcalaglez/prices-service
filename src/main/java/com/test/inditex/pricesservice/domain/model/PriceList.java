package com.test.inditex.pricesservice.domain.model;

public record PriceList(Integer value) {

    public PriceList {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Price list must be positive");
        }
    }

    public static PriceList of(Integer value) {
        return new PriceList(value);
    }

}
