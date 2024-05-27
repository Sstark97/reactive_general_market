package org.example.reactive_general_market.src.shared.infrastructure.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponseDto> handleException(Exception exception) {
  return ResponseEntity.status(HttpStatus.BAD_REQUEST)
    .body(new ErrorResponseDto(HttpStatus.BAD_REQUEST, exception.getMessage()));
  }
}
