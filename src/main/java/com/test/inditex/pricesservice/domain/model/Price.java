package com.test.inditex.pricesservice.domain.model;

import java.util.Objects;

public class Price {

    private final BrandId brandId;
    private final ProductId productId;
    private final PriceList priceList;
    private final DateRange validity;
    private final Priority priority;
    private final Money money;

    private Price(
            BrandId brandId,
            ProductId productId,
            PriceList priceList,
            DateRange validity,
            Priority priority,
            Money money
    ) {
        this.brandId = Objects.requireNonNull(brandId);
        this.productId = Objects.requireNonNull(productId);
        this.priceList = Objects.requireNonNull(priceList);
        this.validity = Objects.requireNonNull(validity);
        this.priority = Objects.requireNonNull(priority);
        this.money = Objects.requireNonNull(money);
    }

    public static Price create(
            BrandId brandId,
            ProductId productId,
            PriceList priceList,
            DateRange validity,
            Priority priority,
            Money money
    ) {
        return new Price(brandId, productId, priceList, validity, priority, money);
    }

    public BrandId brandId() {
        return brandId;
    }

    public ProductId productId() {
        return productId;
    }

    public PriceList priceList() {
        return priceList;
    }

    public DateRange validity() {
        return validity;
    }

    public Priority priority() {
        return priority;
    }

    public Money money() {
        return money;
    }

}
