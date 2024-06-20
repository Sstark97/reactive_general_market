package org.example.reactive_general_market.src.users.application.usecases;

import org.example.reactive_general_market.src.users.application.models.CreateUserCommand;
import org.example.reactive_general_market.src.users.domain.models.User;
import org.example.reactive_general_market.src.users.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateUser {

    private final UserRepository repository;

    public CreateUser(UserRepository repository) {
        this.repository = repository;
    }

    public Mono<User> execute(CreateUserCommand userCommand) {
        var user = new User(null, userCommand.username(), userCommand.password(), userCommand.email());
        return repository.save(user);
    }
}
