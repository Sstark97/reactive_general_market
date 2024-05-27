package org.example.reactive_general_market.src.shared.infrastructure.exceptions;

import org.springframework.http.HttpStatus;

public record ErrorResponseDto(
    HttpStatus status,
    String message
) {}
