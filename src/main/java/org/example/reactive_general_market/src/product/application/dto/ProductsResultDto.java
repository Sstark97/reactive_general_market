package org.example.reactive_general_market.src.product.application.dto;

import java.util.List;

import org.example.reactive_general_market.src.product.domain.model.Product;

public record ProductsResultDto(
    List<Product> products,
    Long totalProducts
) {}
