package org.example.reactive_general_market.src.product.application;

import java.util.List;
import java.util.UUID;

import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindPaginatedProductsTest {
  @Mock
  private ProductRepository productRepository;
  @InjectMocks
  private FindPaginatedProducts findPaginatedProducts;

  @Test
  void should_given_a_list_of_paginated_products(){
    final var products =List.of(
        new Product(UUID.randomUUID(), "first product", "First Product" , 100.0),
        new Product(UUID.randomUUID(), "second product", "Second Product",200.0),
        new Product(UUID.randomUUID(), "third product", "Thirst Product",300.0),
        new Product(UUID.randomUUID(), "fourth product", "Fourth Product",400.0),
        new Product(UUID.randomUUID(), "fifth product", "Five Product",500.0)
    );
    final Pageable pageable = Pageable.ofSize(5).withPage(0);

    when(productRepository.findAll(pageable)).thenReturn(Flux.fromIterable(products));
    when(productRepository.count()).thenReturn(Mono.just(5L));

    findPaginatedProducts.execute(pageable)
        .as(StepVerifier::create)
        .expectNextMatches(productsResultDto -> {
          final var productsResult = productsResultDto.products();
          return productsResult.size() == 5 &&
              productsResult.get(0).name().equals("first product") &&
              productsResult.get(1).name().equals("second product") &&
              productsResult.get(2).name().equals("third product") &&
              productsResult.get(3).name().equals("fourth product") &&
              productsResult.get(4).name().equals("fifth product");
        })
        .verifyComplete();
  }
}