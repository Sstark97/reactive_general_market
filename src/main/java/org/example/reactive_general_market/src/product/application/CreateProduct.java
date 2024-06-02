package org.example.reactive_general_market.src.product.application;

import org.example.reactive_general_market.src.product.application.dto.ProductDto;
import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CreateProduct {
  private final ProductRepository productRepository;

  public CreateProduct(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Mono<Product> execute(ProductDto productDto) {
    if(productDto.name() == null) {
      return Mono.error(new IllegalArgumentException("Required parameters are missing."));
    }
    return productRepository.save(productDto.toCreatedProduct());
  }
}
