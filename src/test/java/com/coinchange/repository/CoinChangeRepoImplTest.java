package com.coinchange.repository;

import com.coinchange.entity.Coin;
import com.coinchange.exception.CoinChangeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoinChangeRepoImplTest {

    private CoinChangeJpaRepository coinChangeJpaRepository;
    private CoinChangeRepoImpl coinChangeRepoImpl;

    @BeforeEach
    void setUp() {
        coinChangeJpaRepository = mock(CoinChangeJpaRepository.class);
        coinChangeRepoImpl = new CoinChangeRepoImpl(coinChangeJpaRepository);
    }

    @Test
    void testGetAvailableCoins() {
        // Given
        Coin coin1 = new Coin(1l,0.25, 10); // Assuming Coin has a constructor that takes denomination and quantity
        Coin coin2 = new Coin(2l,0.10, 5);
        when(coinChangeJpaRepository.findAll()).thenReturn(Arrays.asList(coin1, coin2));

        // When
        Map<Double, Integer> availableCoins = coinChangeRepoImpl.getAvailableCoins();

        // Then
        assertEquals(2, availableCoins.size());
        assertEquals(10, availableCoins.get(0.25));
        assertEquals(5, availableCoins.get(0.10));
    }

    @Test
    void testUpdateCoins_Success() throws CoinChangeException {
        // Given
        Coin coin = new Coin(1l,0.25, 10);
        when(coinChangeJpaRepository.findByDenomination(0.25)).thenReturn(coin);

        Map<Double, Integer> coinsToUpdate = new HashMap<>();
        coinsToUpdate.put(0.25, 5);

        // When
        coinChangeRepoImpl.updateCoins(coinsToUpdate);

        // Then
        assertEquals(5, coin.getQuantity());
        verify(coinChangeJpaRepository, times(1)).save(coin);
    }

    @Test
    void testUpdateCoins_CoinNotFound() {
        // Given
        Map<Double, Integer> coinsToUpdate = new HashMap<>();
        coinsToUpdate.put(0.25, 5);
        when(coinChangeJpaRepository.findByDenomination(0.25)).thenReturn(null);

        // When & Then
        CoinChangeException exception = assertThrows(CoinChangeException.class, () -> {
            coinChangeRepoImpl.updateCoins(coinsToUpdate);
        });
        assertEquals("Coin not found", exception.getMessage());
    }

    @Test
    void testUpdateCoins_SaveInvoked() throws CoinChangeException {
        // Given
        Coin coin = new Coin(1l,0.25, 10);
        when(coinChangeJpaRepository.findByDenomination(0.25)).thenReturn(coin);

        Map<Double, Integer> coinsToUpdate = new HashMap<>();
        coinsToUpdate.put(0.25, 5);

        // When
        coinChangeRepoImpl.updateCoins(coinsToUpdate);

        // Then
        verify(coinChangeJpaRepository).save(coin);
    }
}
