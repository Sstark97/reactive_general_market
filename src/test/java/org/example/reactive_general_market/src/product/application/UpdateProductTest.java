package org.example.reactive_general_market.src.product.application;

import java.util.UUID;

import org.example.reactive_general_market.src.product.application.dto.UpdatedProductDto;
import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateProductTest {

  @Mock
  private ProductRepository productRepository;
  @InjectMocks
  private UpdateProduct updateProduct;

  @Test
  void update_a_product_with_a_valid_fields() {
    final UUID productId = UUID.randomUUID();
    UpdatedProductDto productDto = new UpdatedProductDto(
        productId,
        "Updated Product Name",
        "Product Description",
        100.0
    );
    when(productRepository.update(productDto.toDomain())).thenReturn(Mono.just(productDto.toDomain()));

    updateProduct.execute(productDto)
        .as(StepVerifier::create)
        .assertNext(updatedProduct -> {
          assertEquals(productDto.productId(), updatedProduct.id());
          assertEquals(productDto.updatedProductName(), updatedProduct.name());
          assertEquals(productDto.productDescription(), updatedProduct.description());
          assertEquals(productDto.price(), updatedProduct.price());
        })
        .verifyComplete();
  }

  @Test
  void not_update_a_product_that_not_exists() {
    final UUID productId = UUID.randomUUID();
    UpdatedProductDto productDto = new UpdatedProductDto(
        productId,
        "Updated Product Name",
        "Product Description",
        100.0
    );
    when(productRepository.update(productDto.toDomain())).thenReturn(Mono.empty());

    updateProduct.execute(productDto)
        .as(StepVerifier::create)
        .verifyComplete();
  }
}