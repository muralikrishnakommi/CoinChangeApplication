package com.coinchange.controller;

import com.coinchange.constants.CoinChangeStatusCodes;
import com.coinchange.model.APIResponse;
import com.coinchange.model.CoinChangeResponse;
import com.coinchange.service.CoinChangeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class CoinChangeControllerTest {
    @InjectMocks
    private CoinChangeController coinChangeController;

    @Mock
    private CoinChangeService coinChangeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

  @Test
  void calculateChange_ShouldReturnSuccessResponse() {
        //Arrange
        int bill = 10;
        CoinChangeResponse changeResponse = new CoinChangeResponse();
        when(coinChangeService.requestChange(bill)).thenReturn(changeResponse);
        //Act
        ResponseEntity<APIResponse> response = coinChangeController.calculateChange(bill);
        //Assert (Should return success response)
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(CoinChangeStatusCodes.SUCCESS, Objects.requireNonNull(response.getBody()).getStatus());
        assertEquals(changeResponse, response.getBody().getResults());
    }

}
