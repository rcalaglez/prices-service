package com.test.inditex.pricesservice.infrastructure.adapter.in.web;

import com.test.inditex.pricesservice.application.port.in.GetApplicablePriceUseCase;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/prices")
public class PriceController {

    private final GetApplicablePriceUseCase getApplicablePriceUseCase;
    private final PriceRestMapper mapper;

    public PriceController(
            GetApplicablePriceUseCase getApplicablePriceUseCase,
            PriceRestMapper mapper
    ) {
        this.getApplicablePriceUseCase = getApplicablePriceUseCase;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime applicationDate,

            @RequestParam
            Long productId,

            @RequestParam
            Long brandId
    ) {
        var query = mapper.toQuery(applicationDate, productId, brandId);
        var result = getApplicablePriceUseCase.getApplicablePrice(query);

        return ResponseEntity.ok(mapper.toResponse(result));
    }

}
