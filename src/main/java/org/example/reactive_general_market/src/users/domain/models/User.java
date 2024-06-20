package org.example.reactive_general_market.src.users.domain.models;

public record User(
    String id,
    String name,
    String email,
    String password
) {
}
