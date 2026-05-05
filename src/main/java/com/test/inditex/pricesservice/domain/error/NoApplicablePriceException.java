package com.test.inditex.pricesservice.domain.error;

public class NoApplicablePriceException extends RuntimeException {

    public static final String NO_APPLICABLE_PRICE_FOUND = "No applicable price found";

    public NoApplicablePriceException() {
        super(NO_APPLICABLE_PRICE_FOUND);
    }

}
