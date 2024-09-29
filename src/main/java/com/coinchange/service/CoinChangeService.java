package com.coinchange.service;

import com.coinchange.exception.CoinChangeException;
import com.coinchange.inventory.*;
import com.coinchange.model.CoinChangeResponse;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class CoinChangeService {
  private static final List<Integer> ACCEPTED_BILLS = Arrays.asList(1, 2, 5, 10, 20, 50, 100);
  private final CoinChangeInventory coinInventory;

  public CoinChangeResponse requestChange(final int bill) {

    validateBill(bill);
    CoinChangeResponse response = new CoinChangeResponse();
    final Map<Double, Integer> availableInventory = coinInventory.getAvailableCoins();

    if (availableInventory.isEmpty()) {
      throw new CoinChangeException("No coins available to make change.");
    }

    final Map<Double, Integer> changeMap = new HashMap<>(); // Result map for coins used
    final AtomicReference<Integer> amount = new AtomicReference<>(bill);

    // Iterate over the coin types using streams (sorted descending for the greedy approach)
    availableInventory.keySet().stream()
        .sorted(Comparator.reverseOrder()) // Sort coins in descending order
        .forEach(
            coin -> {
              final double amountRemaining = amount.get();
              final int neededCoins =
                  (int) (amountRemaining / coin); // How many of this coin are needed
              final int availableCoins = availableInventory.get(coin); // How many are available

              if (neededCoins > 0 && availableCoins > 0) {
                // Use as many coins as possible, up to the available amount
                int coinsToUse = Math.min(neededCoins, availableCoins);

                // Add the coins to the change map
                changeMap.put(coin, coinsToUse);

                // Deduct the coins from the inventory
                coinInventory.updateInventory(coin, coinsToUse);

                // Reduce the remaining amount
                amount.getAndUpdate(value -> (int) (value - coinsToUse * coin));
              }
            });

    if (amount.get() > 0) {
      throw new CoinChangeException("Not enough coins to make exact change.");
    }
    response.setCoinChange(changeMap);
    return response;
  }

  private void validateBill(int bill) {
    if (!ACCEPTED_BILLS.contains(bill)) {
      throw new CoinChangeException("Invalid bill. Accepted bills are:" + ACCEPTED_BILLS);
    }
  }
}
