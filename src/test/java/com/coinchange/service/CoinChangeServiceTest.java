package com.coinchange.service;

import com.coinchange.exception.CoinChangeException;
import com.coinchange.inventory.CoinChangeInventory;
import com.coinchange.model.CoinChangeResponse;
import com.coinchange.repository.CoinChangeRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CoinChangeServiceTest {

    @Mock
    private CoinChangeInventory coinInventory;

    @Mock
    private CoinChangeRepo coinChangeRepo;

    @InjectMocks
    private CoinChangeService coinChangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRequestChangeSuccess() {
        //Arrange
        when(coinInventory.getAvailableCoins()).thenReturn(getAvailableCoins());

        // Act
        CoinChangeResponse response = coinChangeService.requestChange(5);
        verify(coinInventory).updateInventory(0.25, 20);
        verify(coinInventory, never()).updateInventory(0.25, 10);

        // Assert that the response is correct
        Map<Double, Integer> expectedChange = new HashMap<>();
        expectedChange.put(0.25, 20);
        assertEquals(expectedChange, response.getCoinChange());
    }

    @Test
    void testRequestChangeWithAllDenominations() {
        //Arrange
        when(coinInventory.getAvailableCoins()).thenReturn(getAvailableCoins());

        // Act
        CoinChangeResponse response = coinChangeService.requestChange(20);
        verify(coinInventory).updateInventory(0.25, 80);

        // Assert that the response is correct
        Map<Double, Integer> expectedChange = new HashMap<>();
        expectedChange.put(0.25, 80);
        assertEquals(expectedChange, response.getCoinChange());
    }

    @Test
    void testRequestChangeExactMatch() {
        // Mock the coin inventory with exact matching coins
        Map<Double, Integer> availableCoins = new HashMap<>();
        availableCoins.put(0.25, 20);
        when(coinInventory.getAvailableCoins()).thenReturn(availableCoins);

        // Call the service
        CoinChangeResponse response = coinChangeService.requestChange(5); // Need exact 5

        // Verify that the inventory update method was called
        verify(coinInventory).updateInventory(0.25, 20);

        // Assert the result
        Map<Double, Integer> expectedChange = new HashMap<>();
        expectedChange.put(0.25, 20);
        assertEquals(expectedChange, response.getCoinChange());
    }

    @Test
    void testRequestChangeNotEnoughCoins() {
        // Mock the coin inventory with insufficient coins
        Map<Double, Integer> availableCoins = new HashMap<>();
        availableCoins.put(0.25, 3); // Only 3 coins of 0.25
        when(coinInventory.getAvailableCoins()).thenReturn(availableCoins);

         // Expect exception
        assertThrows(CoinChangeException.class, () -> coinChangeService.requestChange(5));

        // Verify that no updates happened to the inventory
        verify(coinChangeRepo, never()).updateCoins(any());
    }

    @Test
    void testRequestChangeNoCoinsAvailable() {
        // Mock the coin inventory as empty
        when(coinInventory.getAvailableCoins()).thenReturn(new HashMap<>());

        // Expect exception
        assertThrows(CoinChangeException.class, () -> coinChangeService.requestChange(5));

        // Verify that no updates happened to the inventory
        verify(coinInventory, never()).updateInventory(anyDouble(), anyInt());
    }

    @Test
    void testInvalidBill() {
        // Expect exception for an invalid bill
        assertThrows(CoinChangeException.class, () -> coinChangeService.requestChange(3));
    }

    private Map<Double, Integer> getAvailableCoins(){
        Map<Double, Integer> availableCoins = new HashMap<>();
        availableCoins.put(0.01, 100);
        availableCoins.put(0.05, 100);
        availableCoins.put(0.10, 100);
        availableCoins.put(0.25, 100);
        return availableCoins;
    }
}
