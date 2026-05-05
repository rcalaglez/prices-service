package com.test.inditex.pricesservice.domain.model;

import java.time.LocalDateTime;

public record DateRange(LocalDateTime startDate, LocalDateTime endDate) {

    public DateRange {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Date range boundaries are required");
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    public static DateRange between(LocalDateTime startDate, LocalDateTime endDate) {
        return new DateRange(startDate, endDate);
    }

    public boolean contains(LocalDateTime date) {
        if (date == null) {
            return false;
        }

        return !date.isBefore(startDate) && !date.isAfter(endDate);
    }

}
