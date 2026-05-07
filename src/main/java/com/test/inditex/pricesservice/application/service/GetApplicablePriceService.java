package com.test.inditex.pricesservice.application.service;

import com.test.inditex.pricesservice.application.port.in.GetApplicablePriceUseCase;
import com.test.inditex.pricesservice.application.port.out.PriceRepository;
import com.test.inditex.pricesservice.application.query.GetApplicablePriceQuery;
import com.test.inditex.pricesservice.application.result.ApplicablePriceResult;
import com.test.inditex.pricesservice.domain.model.ApplicablePrices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetApplicablePriceService implements GetApplicablePriceUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(GetApplicablePriceService.class);

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

        LOGGER.debug(
                "Applicable price candidates found. candidatesCount={}",
                candidates.size()
        );

        var selectedPrice = new ApplicablePrices(candidates).highestPriority();

        LOGGER.info(
                "Applicable price selected: productId={}, brandId={}, priceList={}, price={}, currency={}",
                selectedPrice.productId().value(),
                selectedPrice.brandId().value(),
                selectedPrice.priceList().value(),
                selectedPrice.money().amount(),
                selectedPrice.money().currency().getCurrencyCode()
        );

        return new ApplicablePriceResult(
                selectedPrice.productId(),
                selectedPrice.brandId(),
                selectedPrice.priceList(),
                selectedPrice.validity(),
                selectedPrice.money()
        );
    }
}
