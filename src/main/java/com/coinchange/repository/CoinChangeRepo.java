package com.coinchange.repository;

import java.util.Map;

public interface CoinChangeRepo {
  Map<Double, Integer> getAvailableCoins();

  void updateCoins(final Map<Double, Integer> coins);
}
