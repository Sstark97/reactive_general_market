package org.example.reactive_general_market.src.users.domain.repository;

import org.example.reactive_general_market.src.users.domain.models.User;
import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<User> save(User user);
}
