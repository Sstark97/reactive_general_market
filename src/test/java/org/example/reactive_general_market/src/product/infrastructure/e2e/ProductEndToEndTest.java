package org.example.reactive_general_market.src.product.infrastructure.e2e;

import java.util.Objects;

import org.example.reactive_general_market.src.product.application.dto.ProductDto;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.example.reactive_general_market.src.product.infrastructure.containers.ReactivePostgresContainer;
import org.example.reactive_general_market.src.product.infrastructure.repository.ProductR2dbcRepository;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@Import(ReactivePostgresContainer.class)
class ProductEndToEndTest {
  @Autowired
  private WebTestClient webTestClient;
  @Autowired
  private ProductR2dbcRepository productR2dbcRepository;

  @Test
  void create_a_product_with_a_valid_request() {
    ProductDto productDto = new ProductDto(
        "Product Name",
        "Product Description",
        100.0
    );

    webTestClient.post()
        .uri("/general_market/api/v1/products/create")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(productDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.name").isEqualTo("Product Name")
        .jsonPath("$.description").isEqualTo("Product Description")
        .jsonPath("$.price").isEqualTo(100.0);
  }

  @Test
  void create_a_product_request_fail_when_not_provide_all_fields() {
    ProductDto productDto = new ProductDto(
        null,
        "Product Description",
        100.0
    );

    webTestClient.post()
        .uri("/general_market/api/v1/products/create")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(productDto)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody()
        .jsonPath("$.message").isEqualTo("Required parameters are missing.");
  }

  @Test
  void update_a_product_with_a_valid_request() {
    ProductDto productToUpdateDto = new ProductDto(
        "Product Updated Name",
        null,
        null
    );

    final Product product = saveProduct();

    webTestClient.put()
        .uri("/general_market/api/v1/products/update/{id}", product.id())
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(productToUpdateDto)
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.name").isEqualTo("Product Updated Name")
        .jsonPath("$.description").isEqualTo("Product Description")
        .jsonPath("$.price").isEqualTo(100.0);
  }

  private @NotNull Product saveProduct() {
    ProductDto productDto = new ProductDto(
        "Product Name",
        "Product Description",
        100.0
    );
    final Product product = productDto.toCreatedProduct();
    return Objects.requireNonNull(productR2dbcRepository.save(product).block());
  }
}
