package org.example.reactive_general_market.src.product.domain;

import org.example.reactive_general_market.src.product.domain.model.Product;
import reactor.core.publisher.Mono;

public interface ProductRepository {

  Mono<Product> save(Product product);
}
