package org.example.reactive_general_market.src.product.infrastructure.adapter;

import java.util.Optional;

import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.example.reactive_general_market.src.product.infrastructure.repository.ProductR2dbcRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
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

  @Override
  public Flux<Product> findAll(Pageable pageable) {
    return productR2dbcRepository.findAll(pageable.getOffset(), pageable.getPageSize());
  }

  @Override
  public Mono<Long> count() {
    return productR2dbcRepository.count();
  }

  @Transactional
  @Override
  public Mono<Product> update(Product product) {
    return productR2dbcRepository.findById(product.id())
        .flatMap(existingProduct -> Mono.just(getProductUpdated(product, existingProduct)))
        .flatMap(productUpdated ->
          productR2dbcRepository.update(
              productUpdated.id(),
              productUpdated.name(),
              productUpdated.description(),
              productUpdated.price()
          ).then(Mono.just(productUpdated))
        );
  }

  private Product getProductUpdated(Product product, Product existingProduct) {
    return new Product(
        existingProduct.id(),
        Optional.ofNullable(product.name()).orElse(existingProduct.name()),
        Optional.ofNullable(product.description()).orElse(existingProduct.description()),
        Optional.ofNullable(product.price()).orElse(existingProduct.price())
    );
  }
}
