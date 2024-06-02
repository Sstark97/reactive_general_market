package org.example.reactive_general_market.src.product.infrastructure.repository;

import java.util.UUID;

import org.example.reactive_general_market.src.product.domain.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ProductR2dbcRepository extends ReactiveCrudRepository<Product, String>{
  @Query("SELECT * FROM product OFFSET :offset LIMIT :limit")
  Flux<Product> findAll(long offset, int limit);

  @Query("SELECT * FROM product WHERE id = :id")
  Mono<Product> findById(UUID id);

  @Query("UPDATE product SET name = :name, description = :description, price = :price WHERE id = :id")
  Mono<Void> update(UUID id, String name, String description, Double price);

  @Query("DELETE FROM product WHERE id = :id")
  Mono<Void> deleteById(UUID id);
}
