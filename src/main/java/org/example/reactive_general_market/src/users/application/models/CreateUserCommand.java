package org.example.reactive_general_market.src.users.application.models;

public record CreateUserCommand(
    String username,
    String email,
    String password
) {
}
