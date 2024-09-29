package com.coinchange.inventory;

import com.coinchange.entity.Coin;
import com.coinchange.exception.CoinChangeException;
import com.coinchange.repository.CoinChangeJpaRepository;
import com.coinchange.repository.CoinChangeRepoImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoinChangeInventoryTest {

    @Mock
    private CoinChangeRepoImpl coinChangeRepo;

    @Mock
    private CoinChangeJpaRepository coinChangeJpaRepository;

    @InjectMocks
    private CoinChangeInventory coinChangeInventory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableCoins() {
        // Mock the behavior of coinChangeRepo
        Map<Double, Integer> mockedInventory = new HashMap<>();
        mockedInventory.put(0.01, 10);
        mockedInventory.put(0.05, 20);
        when(coinChangeRepo.getAvailableCoins()).thenReturn(mockedInventory);

        // Call the method
        Map<Double, Integer> availableCoins = coinChangeInventory.getAvailableCoins();

        // Verify the result
        assertNotNull(availableCoins);
        assertEquals(10, availableCoins.get(0.01));
        assertEquals(20, availableCoins.get(0.05));

        // Verify that the repo method was called
        verify(coinChangeRepo, times(1)).getAvailableCoins();
    }

    @Test
    void testUpdateInventory() {
        // Mock the initial coin inventory in the repository
        Map<Double, Integer> mockedInventory = new HashMap<>();
        mockedInventory.put(0.01, 10);
        mockedInventory.put(0.05, 20);
        when(coinChangeRepo.getAvailableCoins()).thenReturn(mockedInventory);

        // Call the updateInventory method
        coinChangeInventory.updateInventory(0.01, 5);

        // Verify that the updateCoins method was called with the correct parameters
        Map<Double, Integer> expectedUpdate = Map.of(0.01, 5);  // 10 - 5 = 5 coins of 1.0 remaining
        verify(coinChangeRepo, times(1)).updateCoins(expectedUpdate);
    }

    @Test
    void testUpdateInventoryWithZeroCoinsLeft() {
        // Mock the initial coin inventory in the repository
        Map<Double, Integer> mockedInventory = new HashMap<>();
        mockedInventory.put(0.25, 5);
        when(coinChangeRepo.getAvailableCoins()).thenReturn(mockedInventory);

        // Call the updateInventory method and use all remaining coins
        assertThrows(CoinChangeException.class, () -> coinChangeInventory.updateInventory(0.25, 6));
    }

    @Test
    void testUpdateInventoryThrowsExceptionWhenCoinsUnavailable() {
        // Mock the initial coin inventory with insufficient coins
        Map<Double, Integer> mockedInventory = new HashMap<>();
        mockedInventory.put(0.25, 3);  // Only 3 coins of 0.25
        when(coinChangeRepo.getAvailableCoins()).thenReturn(mockedInventory);
        // Call the updateInventory method with more coins than available
        assertThrows(CoinChangeException.class, () -> coinChangeInventory.updateInventory(0.25, 5));

        // Verify that the updateCoins method was not called because of the exception
        verify(coinChangeRepo, never()).updateCoins(anyMap());
    }
}
