package com.test.inditex.pricesservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domain - ProductId")
class ProductIdTest {

    @Test
    void shouldCreateProductIdWhenValueIsPositive() {
        // Arrange
        var value = 35455L;

        // Act
        var productId = ProductId.of(value);

        // Assert
        assertThat(productId.value()).isEqualTo(value);
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        // Arrange
        Long value = null;

        // Act & Assert
        assertThatThrownBy(() -> ProductId.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product id must be positive");
    }

    @Test
    void shouldThrowExceptionWhenValueIsZero() {
        // Arrange
        var value = 0L;

        // Act & Assert
        assertThatThrownBy(() -> ProductId.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product id must be positive");
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        // Arrange
        var value = -1L;

        // Act & Assert
        assertThatThrownBy(() -> ProductId.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product id must be positive");
    }
}