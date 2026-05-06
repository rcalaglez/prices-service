package com.test.inditex.pricesservice.infrastructure.adapter.out.persistence;

import com.test.inditex.pricesservice.application.port.out.PriceRepository;
import com.test.inditex.pricesservice.domain.model.Price;

import java.time.LocalDateTime;
import java.util.List;

public class PriceJpaAdapter implements PriceRepository {

    private final PriceJpaRepository repository;
    private final PricePersistenceMapper mapper;

    public PriceJpaAdapter(PriceJpaRepository repository, PricePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Price> findCandidates(LocalDateTime applicationDate, Long productId, Long brandId) {
        return repository.findCandidates(
                applicationDate, productId, brandId
                )
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
