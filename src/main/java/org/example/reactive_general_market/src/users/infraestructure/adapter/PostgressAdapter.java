package org.example.reactive_general_market.src.users.infraestructure.adapter;

import org.example.reactive_general_market.src.users.domain.models.User;
import org.example.reactive_general_market.src.users.domain.repository.UserRepository;
import org.example.reactive_general_market.src.users.infraestructure.models.UserEntity;
import org.example.reactive_general_market.src.users.infraestructure.repository.UserPostgresRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PostgressAdapter implements UserRepository {

    private final UserPostgresRepository repository;

    public PostgressAdapter(UserPostgresRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<User> save(User user) {
        UserEntity entity = new UserEntity(user.name(), user.password(), user.email());

        return repository.save(entity)
                .map(userEntity -> new User(user.id(),user.name(), user.email(), user.password()));
    }
}
