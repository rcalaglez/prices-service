package com.test.inditex.pricesservice.infrastructure.config;

import com.test.inditex.pricesservice.application.port.in.GetApplicablePriceUseCase;
import com.test.inditex.pricesservice.application.port.out.PriceRepository;
import com.test.inditex.pricesservice.application.service.GetApplicablePriceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    GetApplicablePriceUseCase getApplicablePriceUseCase(PriceRepository priceRepository) {
        return new GetApplicablePriceService(priceRepository);
    }

}
