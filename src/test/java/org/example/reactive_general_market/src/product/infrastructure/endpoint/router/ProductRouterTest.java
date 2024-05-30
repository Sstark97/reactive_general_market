package org.example.reactive_general_market.src.product.infrastructure.endpoint.router;

import org.example.reactive_general_market.src.product.application.CreateProduct;
import org.example.reactive_general_market.src.product.infrastructure.dto.CreatedProductDto;
import org.example.reactive_general_market.src.product.infrastructure.handler.ProductHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductRouterTest {

  @Mock
  private CreateProduct createProduct;

  @InjectMocks
  private ProductHandler productHandler;

  private WebTestClient webTestClient;

  @BeforeEach
  void setUp() {
    ProductRouter productRouter = new ProductRouter(productHandler);
    webTestClient = WebTestClient.bindToRouterFunction(productRouter.productRoutes()).build();
  }

  @Test
  void create_a_product_with_a_valid_fields() {
    CreatedProductDto productDto = new CreatedProductDto(
        "Product Name",
        "Product Description",
        100.0
    );
    when(createProduct.execute(productDto)).thenReturn(Mono.just(productDto.toDomain()));

    webTestClient.post()
        .uri("/products/create")
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
    CreatedProductDto productDto = new CreatedProductDto(
        null,
        "Product Description",
        100.0
    );
    when(createProduct.execute(productDto)).then(invocation -> Mono.error(new IllegalArgumentException("Required parameters are missing.")));

    webTestClient.post()
        .uri("/products/create")
        .bodyValue(productDto)
        .exchange()
        .expectStatus().isBadRequest()
        .expectBody()
        .jsonPath("$.message").isEqualTo("Required parameters are missing.");
  }
}
