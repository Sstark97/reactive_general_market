package org.example.reactive_general_market.src.product.infrastructure.controller.rest;

import org.example.reactive_general_market.src.product.application.CreateProduct;
import org.example.reactive_general_market.src.product.application.FindPaginatedProducts;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.example.reactive_general_market.src.product.infrastructure.dto.CreatedProductDto;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/products")
public class ProductController {
  private final CreateProduct createProduct;
  private final FindPaginatedProducts findPaginatedProducts;

  public ProductController(CreateProduct createProduct, FindPaginatedProducts findPaginatedProducts) {
    this.createProduct = createProduct;
    this.findPaginatedProducts = findPaginatedProducts;
  }

  @PostMapping("/create")
  public Mono<ResponseEntity<Product>> createProduct(@RequestBody CreatedProductDto product) {
    return createProduct.execute(product)
      .map(ResponseEntity::ok);
  }

  @GetMapping("/all")
  public Flux<Product> findAll(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "5") int size) {
    Pageable pageable = Pageable.ofSize(size).withPage(page);
    return findPaginatedProducts.execute(pageable);

  }
}
