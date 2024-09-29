package com.coinchange.repository;

import com.coinchange.entity.Coin;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.coinchange.exception.CoinChangeException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CoinChangeRepoImpl implements CoinChangeRepo {

  private final CoinChangeJpaRepository coinChangeJpaRepository;

  @Override
  public Map<Double, Integer> getAvailableCoins() {
    return coinChangeJpaRepository.findAll().stream()
        .collect(Collectors.toMap(Coin::getDenomination, Coin::getQuantity));
  }

  @Override
  public void updateCoins(Map<Double, Integer> coins) throws CoinChangeException{
    coins.entrySet().stream()
        .map(
            entry -> {
              final Coin coin =
                  Optional.ofNullable(coinChangeJpaRepository.findByDenomination(entry.getKey()))
                      .orElseThrow(() -> new CoinChangeException("Coin not found"));
              coin.setQuantity(entry.getValue());
              return coin;
            })
        .forEach(coinChangeJpaRepository::save);
  }
}
