package com.test.inditex.pricesservice.infrastructure.adapter.out.persistence;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Infrastructure - Unit - Price persistence mapper")
class PricePersistenceMapperTest {

    private final PricePersistenceMapper mapper = new PricePersistenceMapper();

    @Test
    void shouldMapPriceEntityToDomainPrice() {
        // Arrange
        var entity = new PriceEntity();
        entity.setId(1L);
        entity.setBrandId(1L);
        entity.setProductId(35455L);
        entity.setPriceList(2);
        entity.setStartDate(LocalDateTime.of(2020, 6, 14, 15, 0));
        entity.setEndDate(LocalDateTime.of(2020, 6, 14, 18, 30));
        entity.setPriority(1);
        entity.setPrice(new BigDecimal("25.45"));
        entity.setCurrency("EUR");

        // Act
        var price = mapper.toDomain(entity);

        // Assert
        assertThat(price.brandId().value()).isEqualTo(1L);
        assertThat(price.productId().value()).isEqualTo(35455L);
        assertThat(price.priceList().value()).isEqualTo(2);
        assertThat(price.validity().startDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 15, 0));
        assertThat(price.validity().endDate()).isEqualTo(LocalDateTime.of(2020, 6, 14, 18, 30));
        assertThat(price.priority().value()).isEqualTo(1);
        assertThat(price.money().amount()).isEqualByComparingTo("25.45");
        assertThat(price.money().currency().getCurrencyCode()).isEqualTo("EUR");
    }

}