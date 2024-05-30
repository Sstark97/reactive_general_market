package org.example.reactive_general_market.src.product.infrastructure.repository;

import org.example.reactive_general_market.src.product.domain.model.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductR2dbcRepository extends ReactiveCrudRepository<Product, String>{
  @Query("SELECT * FROM product OFFSET :offset LIMIT :limit")
  Flux<Product> findAll(long offset, int limit);
}
