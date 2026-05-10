package com.test.inditex.pricesservice.e2e;

import com.test.inditex.pricesservice.infrastructure.adapter.in.web.ErrorResponse;
import com.test.inditex.pricesservice.infrastructure.adapter.in.web.PriceResponse;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("E2E - Price API")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql("/test-e2e/prices.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PriceApiE2ETest {

    @LocalServerPort
    private int port;

    private RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: request at 10:00 on day 14 for product 35455 and brand 1 returns price list 1")
    void shouldReturnPriceListOneAtTenOnJuneFourteenth() {
        assertApplicablePrice(
                "2020-06-14T10:00:00",
                35455L,
                1L,
                1,
                "2020-06-14T00:00:00",
                "2020-12-31T23:59:59",
                "35.50"
        );
    }

    @Test
    @Order(2)
    @DisplayName("Test 2: request at 16:00 on day 14 for product 35455 and brand 1 returns price list 2")
    void shouldReturnPriceListTwoAtSixteenOnJuneFourteenth() {
        assertApplicablePrice(
                "2020-06-14T16:00:00",
                35455L,
                1L,
                2,
                "2020-06-14T15:00:00",
                "2020-06-14T18:30:00",
                "25.45"
        );
    }

    @Test
    @Order(3)
    @DisplayName("Test 3: request at 21:00 on day 14 for product 35455 and brand 1 returns price list 1")
    void shouldReturnPriceListOneAtTwentyOneOnJuneFourteenth() {
        assertApplicablePrice(
                "2020-06-14T21:00:00",
                35455L,
                1L,
                1,
                "2020-06-14T00:00:00",
                "2020-12-31T23:59:59",
                "35.50"
        );
    }

    @Test
    @Order(4)
    @DisplayName("Test 4: request at 10:00 on day 15 for product 35455 and brand 1 returns price list 3")
    void shouldReturnPriceListThreeAtTenOnJuneFifteenth() {
        assertApplicablePrice(
                "2020-06-15T10:00:00",
                35455L,
                1L,
                3,
                "2020-06-15T00:00:00",
                "2020-06-15T11:00:00",
                "30.50"
        );
    }

    @Test
    @Order(5)
    @DisplayName("Test 5: request at 21:00 on day 16 for product 35455 and brand 1 returns price list 4")
    void shouldReturnPriceListFourAtTwentyOneOnJuneSixteenth() {
        assertApplicablePrice(
                "2020-06-16T21:00:00",
                35455L,
                1L,
                4,
                "2020-06-15T16:00:00",
                "2020-12-31T23:59:59",
                "38.95"
        );
    }

    private void assertApplicablePrice(
            String applicationDate,
            Long productId,
            Long brandId,
            Integer expectedPriceList,
            String expectedStartDate,
            String expectedEndDate,
            String expectedPrice
    ) {
        // Act
        var response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v1/prices")
                        .queryParam("applicationDate", applicationDate)
                        .queryParam("productId", productId)
                        .queryParam("brandId", brandId)
                        .build())
                .retrieve()
                .toEntity(PriceResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(200));
        assertThat(response.getBody()).isNotNull();

        var body = response.getBody();

        assertThat(body.productId()).isEqualTo(productId);
        assertThat(body.brandId()).isEqualTo(brandId);
        assertThat(body.priceList()).isEqualTo(expectedPriceList);
        assertThat(body.startDate()).isEqualTo(LocalDateTime.parse(expectedStartDate));
        assertThat(body.endDate()).isEqualTo(LocalDateTime.parse(expectedEndDate));
        assertThat(body.price()).isEqualByComparingTo(new BigDecimal(expectedPrice));
        assertThat(body.currency()).isEqualTo("EUR");
    }

    @Test
    @Order(6)
    @DisplayName("Should return 404 when no applicable price exists")
    void shouldReturnNotFoundWhenNoApplicablePriceExists() {
        // Arrange
        var applicationDate = "2020-06-14T10:00:00";
        var productId = 99999L;
        var brandId = 1L;

        // Act & Assert
        assertErrorResponse(
                applicationDate,
                productId,
                brandId,
                404,
                "PRICE_NOT_FOUND",
                "No applicable price found"
        );
    }

    @Test
    @Order(7)
    @DisplayName("Should return 400 when data has invalid format")
    void shouldReturnBadRequestWhenApplicationDateHasInvalidFormat() {
        // Arrange
        var applicationDate = "invalid-date";
        var productId = 35455L;
        var brandId = 1L;

        // Act & Assert
        assertErrorResponse(
                applicationDate,
                productId,
                brandId,
                400,
                "INVALID_REQUEST",
                "Invalid request parameter"
        );
    }

    @Test
    @Order(8)
    @DisplayName("Should return 400 when required data is missing")
    void shouldReturnBadRequestWhenRequiredIsMissing() {
        // Arrange
        var applicationDate = "2020-06-14T10:00:00";
        Long productId = null;
        var brandId = 1L;

        // Act & Assert
        assertErrorResponse(
                applicationDate,
                productId,
                brandId,
                400,
                "INVALID_REQUEST",
                "Invalid request parameter"
        );
    }

    private void assertErrorResponse(
            String applicationDate,
            Long productId,
            Long brandId,
            int expectedStatus,
            String expectedCode,
            String expectedMessage
    ) {
        // Act
        var response = restClient.get()
                .uri(uriBuilder -> {
                    var builder = uriBuilder
                            .path("/api/v1/prices")
                            .queryParam("applicationDate", applicationDate);

                    if (productId != null) {
                        builder.queryParam("productId", productId);
                    }

                    if (brandId != null) {
                        builder.queryParam("brandId", brandId);
                    }

                    return builder.build();
                })
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, clientResponse) -> {
                })
                .toEntity(ErrorResponse.class);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(expectedStatus));
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().code()).isEqualTo(expectedCode);
        assertThat(response.getBody().message()).isEqualTo(expectedMessage);
    }
}
