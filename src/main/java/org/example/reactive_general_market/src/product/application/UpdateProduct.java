package org.example.reactive_general_market.src.product.application;

import org.example.reactive_general_market.src.product.application.dto.UpdatedProductDto;
import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UpdateProduct {
  private final ProductRepository productRepository;

  public UpdateProduct(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Mono<Product> execute(UpdatedProductDto productDto) {
    return productRepository.update(productDto.toDomain())
        .switchIfEmpty(Mono.empty());
  }
}
