package com.test.inditex.pricesservice.infrastructure.adapter.out.persistence;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Infrastructure - Integration - Price JPA adapter")
@DataJpaTest
@ActiveProfiles("test")
class PriceJpaAdapterIntegrationTest {

    @Autowired
    private PriceJpaRepository repository;

    private PriceJpaAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new PriceJpaAdapter(
                repository,
                new PricePersistenceMapper()
        );
    }

    @Test
    void shouldFindCandidatesFromDatabaseAndMapThemToDomain() {
        // Arrange
        repository.save(price(
                1L,
                35455L,
                1,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 12, 31, 23, 59, 59),
                0,
                "35.50",
                "EUR"
        ));

        repository.save(price(
                1L,
                35455L,
                2,
                LocalDateTime.of(2020, 6, 14, 15, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                1,
                "25.45",
                "EUR"
        ));

        var applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        // Act
        var candidates = adapter.findCandidates(
                applicationDate,
                35455L,
                1L
        );

        // Assert
        assertThat(candidates).hasSize(2);
        assertThat(candidates)
                .extracting(price -> price.priceList().value())
                .containsExactlyInAnyOrder(1, 2);

        assertThat(candidates)
                .extracting(price -> price.money().currency().getCurrencyCode())
                .containsOnly("EUR");
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoCandidates() {
        // Arrange
        repository.save(price(
                1L,
                35455L,
                1,
                LocalDateTime.of(2020, 6, 14, 0, 0),
                LocalDateTime.of(2020, 6, 14, 10, 0),
                0,
                "35.50",
                "EUR"
        ));

        var applicationDate = LocalDateTime.of(2020, 6, 14, 11, 0);

        // Act
        var candidates = adapter.findCandidates(
                applicationDate,
                35455L,
                1L
        );

        // Assert
        assertThat(candidates).isEmpty();
    }

    private PriceEntity price(
            Long brandId,
            Long productId,
            Integer priceList,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer priority,
            String price,
            String currency
    ) {
        var entity = new PriceEntity();
        entity.setBrandId(brandId);
        entity.setProductId(productId);
        entity.setPriceList(priceList);
        entity.setStartDate(startDate);
        entity.setEndDate(endDate);
        entity.setPriority(priority);
        entity.setPrice(new BigDecimal(price));
        entity.setCurrency(currency);
        return entity;
    }

}