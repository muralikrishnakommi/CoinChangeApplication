package com.coinchange.repository;

import com.coinchange.entity.Coin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinChangeJpaRepository extends JpaRepository<Coin, Double> {
    Coin findByDenomination(final Double denomination);
}
