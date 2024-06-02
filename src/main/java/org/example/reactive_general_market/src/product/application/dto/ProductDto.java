package org.example.reactive_general_market.src.product.application.dto;

import java.util.UUID;

import org.example.reactive_general_market.src.product.domain.model.Product;

public record ProductDto(
    String name,
    String description,
    Double price
) {
  public Product toCreatedProduct() {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("Name is required");
    }
    if (description == null || description.isEmpty()) {
      throw new IllegalArgumentException("Description is required");
    }
    if (price == null) {
      throw new IllegalArgumentException("Price is required");
    }
    return new Product(UUID.randomUUID(), name, description, price);
  }
}
