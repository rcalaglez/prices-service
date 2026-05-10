package com.test.inditex.pricesservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domain - BrandId")
class BrandIdTest {

    @Test
    void shouldCreateBrandIdWhenValueIsPositive() {
        // Arrange
        var value = 1L;

        // Act
        var brandId = BrandId.of(value);

        // Assert
        assertThat(brandId.value()).isEqualTo(value);
    }

    @Test
    void shouldThrowExceptionWhenValueIsNull() {
        // Arrange
        Long value = null;

        // Act & Assert
        assertThatThrownBy(() -> BrandId.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Brand id must be positive");
    }

    @Test
    void shouldThrowExceptionWhenValueIsZero() {
        // Arrange
        var value = 0L;

        // Act & Assert
        assertThatThrownBy(() -> BrandId.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Brand id must be positive");
    }

    @Test
    void shouldThrowExceptionWhenValueIsNegative() {
        // Arrange
        var value = -1L;

        // Act & Assert
        assertThatThrownBy(() -> BrandId.of(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Brand id must be positive");
    }
}