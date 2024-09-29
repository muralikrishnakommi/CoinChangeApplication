package com.coinchange.handler;

import com.coinchange.constants.CoinChangeStatusCodes;
import com.coinchange.exception.CoinChangeException;
import com.coinchange.model.APIResponse;
import com.coinchange.model.CoinChangeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class CoinChangeExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<APIResponse<CoinChangeResponse>> handleMethodArgumentException(
      MethodArgumentNotValidException exception) {
    APIResponse<CoinChangeResponse> response = new APIResponse<>(CoinChangeStatusCodes.FAILED, exception.getMessage());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(CoinChangeException.class)
  public ResponseEntity<APIResponse<CoinChangeResponse>> handleCoinChangException(CoinChangeException exception) {
    APIResponse<CoinChangeResponse> response = new APIResponse<>(CoinChangeStatusCodes.FAILED, exception.getMessage());
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<APIResponse<CoinChangeResponse>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
    APIResponse<CoinChangeResponse> response = new APIResponse<>(CoinChangeStatusCodes.FAILED, "Invalid bill: Must be an integer.");
    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<APIResponse<CoinChangeResponse>> handleGenericException(Exception ex) {
    APIResponse<CoinChangeResponse> response = new APIResponse<>(CoinChangeStatusCodes.FAILED, ex.getMessage(), null);
    return ResponseEntity.internalServerError().body(response);
  }
}
