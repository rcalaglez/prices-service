package com.test.inditex.pricesservice.e2e;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@DisplayName("E2E - Price API")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Sql("/test-e2e/prices.sql")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PriceApiE2ETest {

    private static final String PRICES_ENDPOINT = "/api/v1/prices";

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    @DisplayName("Test 1: request at 10:00 on day 14 for product 35455 and brand 1 returns price list 1")
    void shouldReturnPriceListOneAtTenOnJuneFourteenth() {
        // Arrange
        var applicationDate = "2020-06-14T10:00:00";
        var productId = 35455L;
        var brandId = 1L;

        // Act & Assert
        assertApplicablePrice(
                applicationDate,
                productId,
                brandId,
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
        // Arrange
        var applicationDate = "2020-06-14T16:00:00";
        var productId = 35455L;
        var brandId = 1L;

        // Act & Assert
        assertApplicablePrice(
                applicationDate,
                productId,
                brandId,
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
        // Arrange
        var applicationDate = "2020-06-14T21:00:00";
        var productId = 35455L;
        var brandId = 1L;

        // Act & Assert
        assertApplicablePrice(
                applicationDate,
                productId,
                brandId,
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
        // Arrange
        var applicationDate = "2020-06-15T10:00:00";
        var productId = 35455L;
        var brandId = 1L;

        // Act & Assert
        assertApplicablePrice(
                applicationDate,
                productId,
                brandId,
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
        // Arrange
        var applicationDate = "2020-06-16T21:00:00";
        var productId = 35455L;
        var brandId = 1L;

        // Act & Assert
        assertApplicablePrice(
                applicationDate,
                productId,
                brandId,
                4,
                "2020-06-15T16:00:00",
                "2020-12-31T23:59:59",
                "38.95"
        );
    }

    @Test
    @Order(6)
    @DisplayName("Should return 404 when no applicable price exists")
    void shouldReturnNotFoundWhenNoApplicablePriceExists() {
        // Arrange
        var applicationDate = "2020-06-14T10:00:00";
        var productId = 9999L;
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
    void shouldReturnBadRequestWhenRequiredDataIsMissing() {
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
        var response = given()
                .queryParam("applicationDate", applicationDate)
                .queryParam("productId", productId)
                .queryParam("brandId", brandId)
                .when()
                .get(PRICES_ENDPOINT)
                .then()
                .statusCode(200)
                .extract()
                .jsonPath();

        // Assert
        assertThat(response.getLong("productId")).isEqualTo(productId);
        assertThat(response.getLong("brandId")).isEqualTo(brandId);
        assertThat(response.getInt("priceList")).isEqualTo(expectedPriceList);
        assertThat(response.getString("startDate")).isEqualTo(expectedStartDate);
        assertThat(response.getString("endDate")).isEqualTo(expectedEndDate);
        assertThat(new BigDecimal(response.getString("price"))).isEqualByComparingTo(expectedPrice);
        assertThat(response.getString("currency")).isEqualTo("EUR");
    }

    private void assertErrorResponse(
            String applicationDate,
            Long productId,
            Long brandId,
            int expectedStatus,
            String expectedCode,
            String expectedMessage
    ) {
        // Arrange
        var request = given()
                .queryParam("applicationDate", applicationDate);

        if (productId != null) {
            request.queryParam("productId", productId);
        }

        if (brandId != null) {
            request.queryParam("brandId", brandId);
        }

        // Act & Assert
        request
                .when()
                .get(PRICES_ENDPOINT)
                .then()
                .statusCode(expectedStatus)
                .body("code", equalTo(expectedCode))
                .body("message", equalTo(expectedMessage));
    }

}