package org.example.reactive_general_market.src.product.application;

import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.example.reactive_general_market.src.product.infrastructure.dto.CreatedProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateProductTest {
  @Mock
  private ProductRepository productRepository;
  @InjectMocks
  private CreateProduct createProduct;

  @Test
  void create_new_product() {
    CreatedProductDto productDto = new CreatedProductDto("Product 1","first product", 100.0);
    Product product = productDto.toDomain();

    when(productRepository.save(any(Product.class))).thenReturn(Mono.just(product));

    createProduct.execute(productDto)
      .as(StepVerifier::create)
      .assertNext(createdProduct -> {
        assertEquals(product.name(), createdProduct.name());
        assertEquals(product.description(), createdProduct.description());
        assertEquals(product.price(), createdProduct.price());
      })
      .verifyComplete();
  }

  @Test
  void should_given_an_error_when_a_field_is_not_present() {
    CreatedProductDto productDto = new CreatedProductDto(null, null, 100.0);

    createProduct.execute(productDto)
        .as(StepVerifier::create)
        .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException && throwable.getMessage().equals("Required parameters are missing."))
        .verify();
  }
}