package com.test.inditex.pricesservice.infrastructure.adapter.in.web;

import com.test.inditex.pricesservice.application.port.in.GetApplicablePriceUseCase;
import com.test.inditex.pricesservice.application.result.ApplicablePriceResult;
import com.test.inditex.pricesservice.domain.error.NoApplicablePriceException;
import com.test.inditex.pricesservice.domain.model.BrandId;
import com.test.inditex.pricesservice.domain.model.DateRange;
import com.test.inditex.pricesservice.domain.model.Money;
import com.test.inditex.pricesservice.domain.model.PriceList;
import com.test.inditex.pricesservice.domain.model.ProductId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.comparesEqualTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
@Import({
        PriceRestMapper.class,
        GlobalExceptionHandler.class
})
@DisplayName("Infrastructure - Web Adapter - PriceController")
class PriceControllerTest {

    private static final String PRICES_ENDPOINT = "/api/v1/prices";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetApplicablePriceUseCase getApplicablePriceUseCase;

    @Test
    void shouldReturnApplicablePriceWhenRequestIsValid() throws Exception {
        // Arrange
        var applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);
        var result = applicablePriceResult(
                35455L,
                1L,
                2,
                LocalDateTime.of(2020, 6, 14, 15, 0),
                LocalDateTime.of(2020, 6, 14, 18, 30),
                "25.45",
                "EUR"
        );

        given(getApplicablePriceUseCase.getApplicablePrice(any()))
                .willReturn(result);

        // Act & Assert
        mockMvc.perform(get(PRICES_ENDPOINT)
                        .queryParam("applicationDate", "2020-06-14T16:00:00")
                        .queryParam("productId", "35455")
                        .queryParam("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.brandId").value(1))
                .andExpect(jsonPath("$.priceList").value(2))
                .andExpect(jsonPath("$.startDate").value("2020-06-14T15:00:00"))
                .andExpect(jsonPath("$.endDate").value("2020-06-14T18:30:00"))
                .andExpect(jsonPath("$.price", comparesEqualTo(25.45)))
                .andExpect(jsonPath("$.currency").value("EUR"));

        var queryCaptor = ArgumentCaptor.forClass(
                com.test.inditex.pricesservice.application.query.GetApplicablePriceQuery.class
        );

        verify(getApplicablePriceUseCase).getApplicablePrice(queryCaptor.capture());

        var capturedQuery = queryCaptor.getValue();

        assertThat(capturedQuery.applicationDate()).isEqualTo(applicationDate);
        assertThat(capturedQuery.productId()).isEqualTo(ProductId.of(35455L));
        assertThat(capturedQuery.brandId()).isEqualTo(BrandId.of(1L));
    }

    @Test
    void shouldReturnNotFoundWhenNoApplicablePriceExists() throws Exception {
        // Arrange
        given(getApplicablePriceUseCase.getApplicablePrice(any()))
                .willThrow(new NoApplicablePriceException());

        // Act & Assert
        mockMvc.perform(get(PRICES_ENDPOINT)
                        .queryParam("applicationDate", "2020-06-14T16:00:00")
                        .queryParam("productId", "99999")
                        .queryParam("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("PRICE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value("No applicable price found"));
    }

    @Test
    void shouldReturnBadRequestWhenApplicationDateHasInvalidFormat() throws Exception {
        // Act & Assert
        mockMvc.perform(get(PRICES_ENDPOINT)
                        .queryParam("applicationDate", "2020/06/14 16:00:00")
                        .queryParam("productId", "35455")
                        .queryParam("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid request parameter"));

        verifyNoInteractions(getApplicablePriceUseCase);
    }

    @Test
    void shouldReturnBadRequestWhenProductIdIsNotNumeric() throws Exception {
        // Act & Assert
        mockMvc.perform(get(PRICES_ENDPOINT)
                        .queryParam("applicationDate", "2020-06-14T16:00:00")
                        .queryParam("productId", "invalid-product")
                        .queryParam("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid request parameter"));

        verifyNoInteractions(getApplicablePriceUseCase);
    }

    @Test
    void shouldReturnBadRequestWhenBrandIdIsNotNumeric() throws Exception {
        // Act & Assert
        mockMvc.perform(get(PRICES_ENDPOINT)
                        .queryParam("applicationDate", "2020-06-14T16:00:00")
                        .queryParam("productId", "35455")
                        .queryParam("brandId", "invalid-brand")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid request parameter"));

        verifyNoInteractions(getApplicablePriceUseCase);
    }

    @Test
    void shouldReturnBadRequestWhenApplicationDateIsMissing() throws Exception {
        // Act & Assert
        mockMvc.perform(get(PRICES_ENDPOINT)
                        .queryParam("productId", "35455")
                        .queryParam("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid request parameter"));

        verifyNoInteractions(getApplicablePriceUseCase);
    }

    @Test
    void shouldReturnBadRequestWhenProductIdIsMissing() throws Exception {
        // Act & Assert
        mockMvc.perform(get(PRICES_ENDPOINT)
                        .queryParam("applicationDate", "2020-06-14T16:00:00")
                        .queryParam("brandId", "1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid request parameter"));

        verifyNoInteractions(getApplicablePriceUseCase);
    }

    @Test
    void shouldReturnBadRequestWhenBrandIdIsMissing() throws Exception {
        // Act & Assert
        mockMvc.perform(get(PRICES_ENDPOINT)
                        .queryParam("applicationDate", "2020-06-14T16:00:00")
                        .queryParam("productId", "35455")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value("INVALID_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid request parameter"));

        verifyNoInteractions(getApplicablePriceUseCase);
    }


    private ApplicablePriceResult applicablePriceResult(
            Long productId,
            Long brandId,
            Integer priceList,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String amount,
            String currency
    ) {
        return new ApplicablePriceResult(
                ProductId.of(productId),
                BrandId.of(brandId),
                PriceList.of(priceList),
                DateRange.between(startDate, endDate),
                new Money(new BigDecimal(amount), Currency.getInstance(currency))
        );
    }

}