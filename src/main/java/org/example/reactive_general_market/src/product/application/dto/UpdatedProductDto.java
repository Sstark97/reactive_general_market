package org.example.reactive_general_market.src.product.application.dto;

import java.util.UUID;

import org.example.reactive_general_market.src.product.domain.model.Product;

public record UpdatedProductDto(
    UUID productId,
    String updatedProductName,
    String productDescription,
    Double price
) {

  public Product toDomain() {
    return new Product(productId, updatedProductName, productDescription, price);
  }
}
