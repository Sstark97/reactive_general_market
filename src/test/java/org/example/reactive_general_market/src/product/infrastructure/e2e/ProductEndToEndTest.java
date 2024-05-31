package org.example.reactive_general_market.src.product.infrastructure.e2e;

import org.example.reactive_general_market.src.product.application.dto.CreatedProductDto;
import org.example.reactive_general_market.src.product.infrastructure.containers.ReactivePostgresContainer;
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

  @Test
  void create_a_product_with_a_valid_request() {
    CreatedProductDto productDto = new CreatedProductDto(
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
    CreatedProductDto productDto = new CreatedProductDto(
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
}
