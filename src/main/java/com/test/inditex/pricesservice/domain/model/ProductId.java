package com.test.inditex.pricesservice.domain.model;

public record ProductId(Long value) {

    public ProductId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("Product id must be positive");
        }
    }

    public static ProductId of(Long value) {
        return new ProductId(value);
    }

}
