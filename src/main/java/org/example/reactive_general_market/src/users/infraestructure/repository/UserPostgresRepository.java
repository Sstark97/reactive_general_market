package org.example.reactive_general_market.src.users.infraestructure.repository;

import org.example.reactive_general_market.src.users.infraestructure.models.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserPostgresRepository extends ReactiveCrudRepository<UserEntity,String> {
}
