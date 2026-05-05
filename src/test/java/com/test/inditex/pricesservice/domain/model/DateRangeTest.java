package com.test.inditex.pricesservice.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Domain - Date range")
class DateRangeTest {

    @Test
    void shouldContainStartDateAndEndDate() {
        // Arrange
        var startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        var endDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59);
        var range = DateRange.between(startDate, endDate);

        // Act
        var containsStartDate = range.contains(startDate);
        var containsEndDate = range.contains(endDate);

        // Assert
        assertThat(containsStartDate).isTrue();
        assertThat(containsEndDate).isTrue();
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() {
        // Arrange
        var startDate = LocalDateTime.of(2020, 12, 31, 23, 59, 59);
        var endDate = LocalDateTime.of(2020, 6, 14, 0, 0);

        // Act / Assert
        assertThatThrownBy(() -> DateRange.between(startDate, endDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("End date cannot be before start date");
    }

}