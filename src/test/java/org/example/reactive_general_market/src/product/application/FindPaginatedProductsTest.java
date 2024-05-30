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

    findPaginatedProducts.execute(pageable)
        .as(StepVerifier::create)
        .expectNext(products.get(0))
        .expectNext(products.get(1))
        .expectNext(products.get(2))
        .expectNext(products.get(3))
        .expectNext(products.get(4))
        .verifyComplete();
  }
}