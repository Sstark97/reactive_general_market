package org.example.reactive_general_market.src.users.infraestructure.models;

import org.example.reactive_general_market.src.users.application.models.CreateUserCommand;

public record UserRequest(
    String username,
    String email,
    String password
) {

    CreateUserCommand toCreateUserCommand() {
        return new CreateUserCommand(username,email,password);
    }
}
