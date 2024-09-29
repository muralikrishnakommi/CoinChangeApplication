package com.coinchange.controller;

import com.coinchange.constants.CoinChangeStatusCodes;
import com.coinchange.model.APIResponse;
import com.coinchange.model.CoinChangeResponse;
import com.coinchange.service.CoinChangeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/change")
@AllArgsConstructor
@Slf4j
public class CoinChangeController {

  private final CoinChangeService changeService;

  @GetMapping(value = "/{bill}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<APIResponse> calculateChange(@PathVariable int bill) {
    log.info("Received request to calculate change for bill: {}", bill);
    CoinChangeResponse coinChangeResponse = changeService.requestChange(bill);
    APIResponse<CoinChangeResponse> apiResponse =
        APIResponse.<CoinChangeResponse>builder()
            .status(CoinChangeStatusCodes.SUCCESS)
            .results(coinChangeResponse)
            .build();
    log.info("Change calculation successful: {}", apiResponse);
    return ResponseEntity.ok(apiResponse);
  }
}
