package org.example.reactive_general_market.src.product.application;

import java.util.UUID;

import org.example.reactive_general_market.src.product.domain.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteProductTest {
  @Mock
  private ProductRepository productRepository;
  @InjectMocks
  private DeleteProduct deleteProduct;

  @Test
  void delete_an_existing_product() {
    UUID productId = UUID.randomUUID();

    when(productRepository.deleteById(productId)).thenReturn(Mono.empty());

    StepVerifier.create(deleteProduct.execute(productId))
      .verifyComplete();
  }
}