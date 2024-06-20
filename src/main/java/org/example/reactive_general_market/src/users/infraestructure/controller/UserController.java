package org.example.reactive_general_market.src.users.infraestructure.controller;

import org.example.reactive_general_market.src.users.application.models.CreateUserCommand;
import org.example.reactive_general_market.src.users.application.usecases.CreateUser;
import org.example.reactive_general_market.src.users.domain.models.User;
import org.example.reactive_general_market.src.users.infraestructure.models.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final CreateUser createUser;

    public UserController(CreateUser createUser) {
        this.createUser = createUser;
    }

    @PostMapping
    public ResponseEntity<Mono<User>> createUser(@RequestBody UserRequest user){
        CreateUserCommand createUserCommand = new CreateUserCommand(user.username(), user.email(), user.password());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createUser.execute(createUserCommand));
    }
}
