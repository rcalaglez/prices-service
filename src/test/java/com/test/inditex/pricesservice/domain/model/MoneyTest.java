package com.test.inditex.pricesservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("Domain - Money")
class MoneyTest {

    @Test
    void shouldCreateMoneyUsingAmountAndCurrencyCode() {
        // Arrange
        var amount = new BigDecimal("25.45");
        var currencyCode = "EUR";

        // Act
        var money = Money.of(amount, currencyCode);

        // Assert
        assertThat(money.amount()).isEqualByComparingTo("25.45");
        assertThat(money.currency()).isEqualTo(Currency.getInstance("EUR"));
    }

    @Test
    void shouldAllowZeroAmount() {
        // Arrange
        var amount = BigDecimal.ZERO;
        var currency = Currency.getInstance("EUR");

        // Act
        var money = new Money(amount, currency);

        // Assert
        assertThat(money.amount()).isEqualByComparingTo("0");
        assertThat(money.currency()).isEqualTo(currency);
    }

    @Test
    void shouldThrowExceptionWhenFactoryReceivesNullAmount() {
        // Arrange
        BigDecimal amount = null;
        var currencyCode = "EUR";

        // Act & Assert
        assertThatThrownBy(() -> Money.of(amount, currencyCode))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount is required");
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        // Arrange
        var amount = new BigDecimal("-1.00");
        var currency = Currency.getInstance("EUR");

        // Act & Assert
        assertThatThrownBy(() -> new Money(amount, currency))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Amount cannot be negative");
    }

    @Test
    void shouldThrowExceptionWhenCurrencyCodeIsInvalid() {
        // Arrange
        var amount = new BigDecimal("35.50");
        var currencyCode = "INVALID";

        // Act & Assert
        assertThatThrownBy(() -> Money.of(amount, currencyCode))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldThrowExceptionWhenCurrencyCodeIsNull() {
        // Arrange
        var amount = new BigDecimal("35.50");
        String currencyCode = null;

        // Act & Assert
        assertThatThrownBy(() -> Money.of(amount, currencyCode))
                .isInstanceOf(NullPointerException.class);
    }

}