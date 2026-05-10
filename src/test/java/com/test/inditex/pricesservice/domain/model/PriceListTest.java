package com.test.inditex.pricesservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;


@DisplayName("Domain - PriceList")
class PriceListTest {

    @Test
    void shouldCreatePriceListWhenValueIsPositive() {
        // Arrange
        var value = 1;

        // Act
        var priceList = PriceList.of(value);

        // Assert
        assertThat(priceList.value()).isEqualTo(value);
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        // Arrange
        Integer value = null;

        // Act & Assert
        assertThatThrownBy(() -> PriceList.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price list must be positive");
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        // Arrange
        var value = -1;

        // Act & Assert
        assertThatThrownBy(() -> PriceList.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Price list must be positive");
    }

}