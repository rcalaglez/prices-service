package com.test.inditex.pricesservice.infrastructure.adapter.in.web;

import com.test.inditex.pricesservice.domain.error.NoApplicablePriceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoApplicablePriceException.class)
    public ResponseEntity<ErrorResponse> handleNoApplicablePrice(NoApplicablePriceException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(
                        "PRICE_NOT_FOUND",
                        exception.getMessage()
                ));
    }

    @ExceptionHandler({
            MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception exception) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(
                        "INVALID_REQUEST",
                        "Invalid request parameter"
                ));
    }

}
