package org.example.reactive_general_market.src.product.domain.model;

import java.util.UUID;

public record Product(
    UUID id,
    String name,
    String description,
    Double price
) {}
