package com.test.inditex.pricesservice.domain.model;

public record Priority(Integer value) implements Comparable<Priority> {
    public Priority {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("Priority cannot be negative");
        }
    }

    public static Priority of(Integer value) {
        return new Priority(value);
    }

    @Override
    public int compareTo(Priority other) {
        return this.value.compareTo(other.value);
    }
}
