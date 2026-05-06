package com.test.inditex.pricesservice.application.service;

import com.test.inditex.pricesservice.application.port.in.GetApplicablePriceUseCase;
import com.test.inditex.pricesservice.application.port.out.PriceRepository;
import com.test.inditex.pricesservice.application.query.GetApplicablePriceQuery;
import com.test.inditex.pricesservice.application.result.ApplicablePriceResult;
import com.test.inditex.pricesservice.domain.model.ApplicablePrices;

public class GetApplicablePriceService implements GetApplicablePriceUseCase {

    private final PriceRepository priceRepository;


    public GetApplicablePriceService(PriceRepository priceRepository) {
        this.priceRepository = priceRepository;
    }


    @Override
    public ApplicablePriceResult getApplicablePrice(GetApplicablePriceQuery query) {
        var candidates = priceRepository.findCandidates(
                query.applicationDate(),
                query.productId().value(),
                query.brandId().value()
        );

        var selectedPrice = new ApplicablePrices(candidates).highestPriority();

        return new ApplicablePriceResult(
                selectedPrice.productId(),
                selectedPrice.brandId(),
                selectedPrice.priceList(),
                selectedPrice.validity(),
                selectedPrice.money()
        );
    }
}
