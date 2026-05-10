package com.test.inditex.pricesservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domain - Price")
class PriceTest {

    @Test
    void shouldCreatePriceWhenAllMandatoryFieldsAreProvided() {
        // Arrange
        var brandId = BrandId.of(1L);
        var productId = ProductId.of(35455L);
        var priceList = PriceList.of(1);
        var validity = validDateRange();
        var priority = Priority.of(0);
        var money = new Money(new BigDecimal("35.50"), Currency.getInstance("EUR"));

        // Act
        var price = Price.create(
                brandId,
                productId,
                priceList,
                validity,
                priority,
                money
        );

        // Assert
        assertThat(price.brandId()).isEqualTo(brandId);
        assertThat(price.productId()).isEqualTo(productId);
        assertThat(price.priceList()).isEqualTo(priceList);
        assertThat(price.validity()).isEqualTo(validity);
        assertThat(price.priority()).isEqualTo(priority);
        assertThat(price.money()).isEqualTo(money);
    }

    @Test
    void shouldThrowExceptionWhenBrandIdIsNull() {
        // Arrange
        BrandId brandId = null;

        // Act & Assert
        assertThatThrownBy(() -> Price.create(
                brandId,
                ProductId.of(35455L),
                PriceList.of(1),
                validDateRange(),
                Priority.of(0),
                money()
        ))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNull() {
        // Arrange
        ProductId productId = null;

        // Act & Assert
        assertThatThrownBy(() -> Price.create(
                BrandId.of(1L),
                productId,
                PriceList.of(1),
                validDateRange(),
                Priority.of(0),
                money()
        ))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenPriceListIsNull() {
        // Arrange
        PriceList priceList = null;

        // Act & Assert
        assertThatThrownBy(() -> Price.create(
                BrandId.of(1L),
                ProductId.of(35455L),
                priceList,
                validDateRange(),
                Priority.of(0),
                money()
        ))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenValidityIsNull() {
        // Arrange
        DateRange validity = null;

        // Act & Assert
        assertThatThrownBy(() -> Price.create(
                BrandId.of(1L),
                ProductId.of(35455L),
                PriceList.of(1),
                validity,
                Priority.of(0),
                money()
        ))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenPriorityIsNull() {
        // Arrange
        Priority priority = null;

        // Act & Assert
        assertThatThrownBy(() -> Price.create(
                BrandId.of(1L),
                ProductId.of(35455L),
                PriceList.of(1),
                validDateRange(),
                priority,
                money()
        ))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldThrowExceptionWhenMoneyIsNull() {
        // Arrange
        Money money = null;

        // Act & Assert
        assertThatThrownBy(() -> Price.create(
                BrandId.of(1L),
                ProductId.of(35455L),
                PriceList.of(1),
                validDateRange(),
                Priority.of(0),
                money
        ))
                .isInstanceOf(NullPointerException.class);
    }

    private DateRange validDateRange() {
        return DateRange.between(
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59)
        );
    }

    private Money money() {
        return new Money(new BigDecimal("35.50"), Currency.getInstance("EUR"));
    }

}