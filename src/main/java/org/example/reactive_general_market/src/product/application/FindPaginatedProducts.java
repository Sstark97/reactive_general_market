package org.example.reactive_general_market.src.product.application;

import org.example.reactive_general_market.src.product.application.model.ProductsResultDto;
import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class FindPaginatedProducts {
  private final ProductRepository productRepository;

  public FindPaginatedProducts(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  public Mono<ProductsResultDto> execute(Pageable pageable) {
    return productRepository.findAll(pageable)
      .collectList()
        .flatMap(products -> count().map(count -> new ProductsResultDto(products, count)));
  }

  private Mono<Long> count() {
    return productRepository.count();
  }
}
