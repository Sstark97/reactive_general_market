package org.example.reactive_general_market.src.product.infrastructure.adapter;

import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.example.reactive_general_market.src.product.infrastructure.repository.ProductR2dbcRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ProductPostgresr2dbcAdapter implements ProductRepository {
  private final ProductR2dbcRepository productR2dbcRepository;

  public ProductPostgresr2dbcAdapter(ProductR2dbcRepository productR2dbcRepository) {
    this.productR2dbcRepository = productR2dbcRepository;
  }

  @Override
  public Mono<Product> save(Product product) {
    return productR2dbcRepository.save(product);
  }
}
