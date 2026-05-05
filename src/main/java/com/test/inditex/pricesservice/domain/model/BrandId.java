package com.test.inditex.pricesservice.domain.model;

public record BrandId(Long value) {

    public BrandId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Brand id must be positive");
        }
    }

    public static BrandId of(Long value) {
        return new BrandId(value);
    }

}
