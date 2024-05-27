package org.example.reactive_general_market.src.product.infrastructure.dto;

import java.util.UUID;

import org.example.reactive_general_market.src.product.domain.model.Product;

public record CreatedProductDto(
    String name,
    String description,
    Double price
) {
  public Product toDomain() {
    return new Product(UUID.randomUUID(), name, description, price);
  }
}
