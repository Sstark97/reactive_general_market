package org.example.reactive_general_market.src.product.domain;

import org.example.reactive_general_market.src.product.domain.model.Product;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductRepository {

  Mono<Product> save(Product product);

  Flux<Product> findAll(Pageable pageable);

  Mono<Long> count();

  Mono<Product> update(Product product);
}
