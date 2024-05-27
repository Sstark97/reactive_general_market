package org.example.reactive_general_market.src.product.infrastructure.repository;

import org.example.reactive_general_market.src.product.domain.model.Product;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductR2dbcRepository extends ReactiveCrudRepository<Product, String>{}
