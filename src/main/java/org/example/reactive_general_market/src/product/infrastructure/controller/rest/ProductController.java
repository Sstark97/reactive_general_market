package org.example.reactive_general_market.src.product.infrastructure.controller.rest;

import org.example.reactive_general_market.src.product.application.CreateProduct;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.example.reactive_general_market.src.product.infrastructure.dto.CreatedProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {
  private final CreateProduct createProduct;

  public ProductController(CreateProduct createProduct) {
    this.createProduct = createProduct;
  }

  @PostMapping("/create")
  public Mono<ResponseEntity<Product>> createProduct(@RequestBody CreatedProductDto product) {
    return createProduct.execute(product)
      .map(ResponseEntity::ok);
  }
}
