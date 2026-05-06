package com.test.inditex.pricesservice.infrastructure.config;

import com.test.inditex.pricesservice.application.port.out.PriceRepository;
import com.test.inditex.pricesservice.infrastructure.adapter.in.web.PriceRestMapper;
import com.test.inditex.pricesservice.infrastructure.adapter.out.persistence.PriceJpaAdapter;
import com.test.inditex.pricesservice.infrastructure.adapter.out.persistence.PriceJpaRepository;
import com.test.inditex.pricesservice.infrastructure.adapter.out.persistence.PricePersistenceMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdapterConfig {

    @Bean
    PricePersistenceMapper pricePersistenceMapper() {
        return new PricePersistenceMapper();
    }

    @Bean
    PriceRepository priceRepository(
            PriceJpaRepository repository,
            PricePersistenceMapper mapper
    ) {
        return new PriceJpaAdapter(repository, mapper);
    }

    @Bean
    PriceRestMapper priceRestMapper() {
        return new PriceRestMapper();
    }

}
