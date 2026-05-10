package com.test.inditex.pricesservice.infrastructure.adapter.in.web;

import com.test.inditex.pricesservice.application.result.ApplicablePriceResult;
import com.test.inditex.pricesservice.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Infrastructure - PriceRestMapper")
class PriceRestMapperTest {

    private final PriceRestMapper mapper = new PriceRestMapper();

    @Test
    void shouldMapRequestParametersToApplicationQuery() {
        // Arrange
        var applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        var productId = 35455L;
        var brandId = 1L;

        // Act
        var query = mapper.toQuery(applicationDate, productId, brandId);

        // Assert
        assertThat(query.applicationDate()).isEqualTo(applicationDate);
        assertThat(query.productId()).isEqualTo(ProductId.of(productId));
        assertThat(query.brandId()).isEqualTo(BrandId.of(brandId));
    }

    @Test
    void shouldMapApplicablePriceResultToRestResponse() {
        // Arrange
        var result = new ApplicablePriceResult(
                ProductId.of(35455L),
                BrandId.of(1L),
                PriceList.of(2),
                DateRange.between(
                        LocalDateTime.of(2020, 6, 14, 15, 0),
                        LocalDateTime.of(2020, 6, 14, 18, 30)
                ),
                Money.of(new BigDecimal("25.45"), "EUR")
        );

        // Act
        var response = mapper.toResponse(result);

        // Assert
        assertThat(response.productId()).isEqualTo(35455L);
        assertThat(response.brandId()).isEqualTo(1L);
        assertThat(response.priceList()).isEqualTo(2);
        assertThat(response.startDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 15, 0));
        assertThat(response.endDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 18, 30));
        assertThat(response.price()).isEqualByComparingTo("25.45");
        assertThat(response.currency()).isEqualTo("EUR");
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsNull() {
        // Arrange
        var applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        Long productId = null;
        var brandId = 1L;

        // Act & Assert
        assertThatThrownBy(() -> mapper.toQuery(applicationDate, productId, brandId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product id must be positive");
    }

    @Test
    void shouldThrowExceptionWhenProductIdIsZero() {
        // Arrange
        var applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        var productId = 0L;
        var brandId = 1L;

        // Act & Assert
        assertThatThrownBy(() -> mapper.toQuery(applicationDate, productId, brandId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Product id must be positive");
    }

    @Test
    void shouldThrowExceptionWhenBrandIdIsNull() {
        // Arrange
        var applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        var productId = 35455L;
        Long brandId = null;

        // Act & Assert
        assertThatThrownBy(() -> mapper.toQuery(applicationDate, productId, brandId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Brand id must be positive");
    }

    @Test
    void shouldThrowExceptionWhenBrandIdIsZero() {
        // Arrange
        var applicationDate = LocalDateTime.of(2020, 6, 14, 10, 0);
        var productId = 35455L;
        var brandId = 0L;

        // Act & Assert
        assertThatThrownBy(() -> mapper.toQuery(applicationDate, productId, brandId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Brand id must be positive");
    }
}