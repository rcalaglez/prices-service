package com.test.inditex.pricesservice.domain.model;

import com.test.inditex.pricesservice.domain.error.NoApplicablePriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Domain - Applicable prices")
class ApplicablePricesTest {

    @Test
    void shouldSelectPriceWithHighestPriority() {
        // Arrange
        var lowPriorityPrice = priceWithPriority(0, "35.50");
        var highPriorityPrice = priceWithPriority(1, "25.45");
        var applicablePrices = new ApplicablePrices(List.of(lowPriorityPrice, highPriorityPrice));

        // Act
        var selectedPrice = applicablePrices.highestPriority();

        // Assert
        assertThat(selectedPrice).isEqualTo(highPriorityPrice);
    }

    @Test
    void shouldThrowExceptionWhenThereAreNoApplicablePrices() {
        // Arrange
        var applicablePrices = new ApplicablePrices(List.of());

        // Act / Assert
        assertThatThrownBy(applicablePrices::highestPriority)
                .isInstanceOf(NoApplicablePriceException.class)
                .hasMessage("No applicable price found");
    }

    private Price priceWithPriority(Integer priority, String amount) {
        return Price.create(
                BrandId.of(1L),
                ProductId.of(35455L),
                PriceList.of(1),
                DateRange.between(
                        LocalDateTime.of(2020, 6, 14, 0, 0),
                        LocalDateTime.of(2020, 12, 31, 23, 59, 59)
                ),
                Priority.of(priority),
                new Money(new BigDecimal(amount), Currency.getInstance("EUR"))
        );
    }

}