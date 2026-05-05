package com.test.inditex.pricesservice.application.port.in;

import com.test.inditex.pricesservice.application.query.GetApplicablePriceQuery;
import com.test.inditex.pricesservice.application.result.ApplicablePriceResult;

public interface GetApplicablePriceUseCase {
    ApplicablePriceResult getApplicablePrice(GetApplicablePriceQuery query);
}
