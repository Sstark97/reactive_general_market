package org.example.reactive_general_market.src.product.application.model;

import java.util.List;

import org.example.reactive_general_market.src.product.domain.model.Product;

public record ProductPage(
    List<Product> content,
    int totalElements,
    int totalPages,
    int pageNumber,
    int pageElementsSize,
    boolean first,
    boolean last,
    boolean empty
) {}
