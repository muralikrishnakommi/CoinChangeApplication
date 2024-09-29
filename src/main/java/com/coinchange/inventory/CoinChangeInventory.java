package com.coinchange.inventory;

import com.coinchange.exception.CoinChangeException;
import com.coinchange.repository.CoinChangeRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class CoinChangeInventory {
    private final CoinChangeRepo coinChangeRepo;

    public Map<Double, Integer> getAvailableCoins() {
        return coinChangeRepo.getAvailableCoins();
    }

    // Update the inventory after a transaction
    public void updateInventory(final double coin, final int coinsUsed) {
        final Map<Double, Integer> coinInventory = coinChangeRepo.getAvailableCoins();
        if((coinInventory.get(coin) - coinsUsed) < 0){
            throw new CoinChangeException("Not enough coins to make exact change.");
        }
        coinChangeRepo.updateCoins(Map.of(coin, coinInventory.get(coin) - coinsUsed));
    }


}
