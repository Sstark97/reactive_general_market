package org.example.reactive_general_market.src.product.application;

import java.util.UUID;

import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DeleteProduct {
  private final ProductRepository productRepository;

  public DeleteProduct(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Mono<Void> execute(UUID productId) {
    return productRepository.deleteById(productId);
  }
}
