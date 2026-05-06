package com.test.inditex.pricesservice.e2e;

import com.test.inditex.pricesservice.infrastructure.adapter.in.web.PriceResponse;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

        log.info("Request params: applicationDate {}, productId {}, brandId {} ",
                applicationDate,
                productId,
                brandId);
        var body = response.getBody();
        log.info("Response: {}", body);

        assertThat(body.productId()).isEqualTo(productId);
        assertThat(body.brandId()).isEqualTo(brandId);
        assertThat(body.priceList()).isEqualTo(expectedPriceList);
        assertThat(body.startDate()).isEqualTo(LocalDateTime.parse(expectedStartDate));
        assertThat(body.endDate()).isEqualTo(LocalDateTime.parse(expectedEndDate));
        assertThat(body.price()).isEqualByComparingTo(new BigDecimal(expectedPrice));
        assertThat(body.currency()).isEqualTo("EUR");
    }
}
