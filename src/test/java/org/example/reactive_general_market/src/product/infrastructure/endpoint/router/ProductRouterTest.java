package org.example.reactive_general_market.src.product.infrastructure.endpoint.router;

import java.util.List;
import java.util.UUID;

import org.example.reactive_general_market.src.product.application.CreateProduct;
import org.example.reactive_general_market.src.product.application.FindPaginatedProducts;
import org.example.reactive_general_market.src.product.application.UpdateProduct;
import org.example.reactive_general_market.src.product.application.dto.ProductDto;
import org.example.reactive_general_market.src.product.application.dto.ProductsResultDto;
import org.example.reactive_general_market.src.product.application.dto.UpdatedProductDto;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.example.reactive_general_market.src.product.infrastructure.handler.ProductHandler;
import org.example.reactive_general_market.src.product.infrastructure.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRouterTest {

  @Mock
  private CreateProduct createProduct;
  @Mock
  private FindPaginatedProducts findPaginatedProducts;
  @Mock
  private UpdateProduct updateProduct;

  @InjectMocks
  private ProductHandler productHandler;

  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    ProductRouter productRouter = new ProductRouter(productHandler);
    webTestClient = WebTestClient.bindToRouterFunction(productRouter.routes()).build();
  }

  @Test
  void create_a_product_with_a_valid_fields() {
    ProductDto productDto = new ProductDto(
        "Product Name",
        "Product Description",
        100.0
    );
    when(createProduct.execute(productDto)).thenReturn(Mono.just(productDto.toCreatedProduct()));

    webTestClient.post()
        .uri("/general_market/api/v1/products/create")
        .bodyValue(productDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.name").isEqualTo("Product Name")
        .jsonPath("$.description").isEqualTo("Product Description")
        .jsonPath("$.price").isEqualTo(100.0);
  }

  @Test
  void create_a_product_fail_when_not_provide_all_fields() {
    ProductDto productDto = new ProductDto(
        null,
        "Product Description",
        100.0
    );
    when(createProduct.execute(productDto)).then(invocation -> Mono.error(new IllegalArgumentException("Required parameters are missing.")));

    webTestClient.post()
        .uri("/general_market/api/v1/products/create")
        .bodyValue(productDto)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody()
        .jsonPath("$.message").isEqualTo("Required parameters are missing.");
  }

  @Test
  void find_all_products_paginated() {
    final var products = List.of(
        new Product(UUID.randomUUID(), "first product", "First Product" , 100.0),
        new Product(UUID.randomUUID(), "second product", "Second Product",200.0),
        new Product(UUID.randomUUID(), "third product", "Thirst Product",300.0),
        new Product(UUID.randomUUID(), "fourth product", "Fourth Product",400.0),
        new Product(UUID.randomUUID(), "fifth product", "Five Product",500.0)
    );
    final Pageable pageable = Pageable.ofSize(5).withPage(0);

    when(findPaginatedProducts.execute(pageable)).thenReturn(Mono.just(new ProductsResultDto(products, 5L)));

    webTestClient.get()
        .uri("/general_market/api/v1/products/all?page=0&size=5")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.content").isArray()
        .jsonPath("$.pageNumber").isEqualTo(0)
        .jsonPath("$.pageElementsSize").isEqualTo(5)
        .jsonPath("$.totalElements").isNumber()
        .jsonPath("$.totalPages").isNumber()
        .jsonPath("$.last").isBoolean();
  }

  @Test
  void update_product_fields() {
    final UUID productId = UUID.randomUUID();
    ProductDto productDto = new ProductDto(
        "Updated Product Name",
        "Product Description",
        100.0
    );
    final UpdatedProductDto updatedProductDto = ProductMapper.toUpdatedProductDto(productDto, productId);
    when(updateProduct.execute(updatedProductDto)).thenReturn(Mono.just(productDto.toCreatedProduct()));

    webTestClient.put()
        .uri("/general_market/api/v1/products/update/{id}", productId)
        .bodyValue(productDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.name").isEqualTo("Updated Product Name")
        .jsonPath("$.description").isEqualTo("Product Description")
        .jsonPath("$.price").isEqualTo(100.0);
  }

  @Test
  void not_allow_to_update_a_product_if_not_exists() {
    final UUID productId = UUID.randomUUID();
    ProductDto productDto = new ProductDto(
        "Updated Product Name",
        "Product Description",
        100.0
    );
    final UpdatedProductDto updatedProductDto = ProductMapper.toUpdatedProductDto(productDto, productId);
    when(updateProduct.execute(updatedProductDto)).thenReturn(Mono.empty());

    webTestClient.put()
        .uri("/general_market/api/v1/products/update/{id}", productId)
        .bodyValue(productDto)
        .exchange()
        .expectStatus().isNotFound();
  }
}
