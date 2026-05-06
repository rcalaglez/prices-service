package com.test.inditex.pricesservice.application.service;

import com.test.inditex.pricesservice.application.port.out.PriceRepository;
import com.test.inditex.pricesservice.application.query.GetApplicablePriceQuery;
import com.test.inditex.pricesservice.domain.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@DisplayName("Application - Unit - Get applicable price")
@ExtendWith(MockitoExtension.class)
class GetApplicablePriceServiceTest {

    private final PriceRepository priceRepository = mock(PriceRepository.class);
    private final GetApplicablePriceService service = new GetApplicablePriceService(priceRepository);

    @Test
    void shouldReturnApplicablePriceWithHighestPriority() {
        // Arrange
        var applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        var productId = ProductId.of(35455L);
        var brandId = BrandId.of(1L);
        var query = new GetApplicablePriceQuery(applicationDate, productId, brandId);

        var basePrice = price(PriceList.of(1), Priority.of(0), "35.50");
        var priorityPrice = price(PriceList.of(2), Priority.of(1), "25.45");

        given(priceRepository.findCandidates(applicationDate, productId.value(), brandId.value()))
                .willReturn(List.of(basePrice, priorityPrice));

        // Act
        var result = service.getApplicablePrice(query);

        // Assert
        assertThat(result.priceList()).isEqualTo(PriceList.of(2));
        assertThat(result.money().amount()).isEqualByComparingTo("25.45");
    }

    private Price price(PriceList priceList, Priority priority, String amount) {
        return Price.create(
                BrandId.of(1L),
                ProductId.of(35455L),
                priceList,
                DateRange.between(
                        LocalDateTime.of(2020, 6, 14, 0, 0),
                        LocalDateTime.of(2020, 12, 31, 23, 59, 59)
                ),
                priority,
                new Money(new BigDecimal(amount), Currency.getInstance("EUR"))
        );
    }

}