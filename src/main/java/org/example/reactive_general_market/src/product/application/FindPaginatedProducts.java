package org.example.reactive_general_market.src.product.application;

import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class FindPaginatedProducts {
  private final ProductRepository productRepository;

  public FindPaginatedProducts(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Flux<Product> execute(Pageable pageable) {
    return productRepository.findAll(pageable);
  }
}
