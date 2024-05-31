package org.example.reactive_general_market.src.product.infrastructure.handler;

import org.example.reactive_general_market.src.product.application.CreateProduct;
import org.example.reactive_general_market.src.product.application.FindPaginatedProducts;
import org.example.reactive_general_market.src.product.application.model.ProductPage;
import org.example.reactive_general_market.src.product.application.model.ProductsResultDto;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.example.reactive_general_market.src.product.infrastructure.dto.CreatedProductDto;
import org.example.reactive_general_market.src.product.infrastructure.dto.ErrorResponse;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {
  private final CreateProduct createProduct;
  private final FindPaginatedProducts findPaginatedProducts;

  public ProductHandler(CreateProduct createProduct, FindPaginatedProducts findPaginatedProducts) {
    this.createProduct = createProduct;
    this.findPaginatedProducts = findPaginatedProducts;
  }

  public Mono<ServerResponse> createProduct(ServerRequest request) {
    return request.bodyToMono(CreatedProductDto.class)
      .flatMap(createProduct::execute)
      .flatMap(createdProduct -> ServerResponse.ok().bodyValue(createdProduct))
        .onErrorResume(error -> ServerResponse.badRequest().bodyValue(new ErrorResponse(error.getMessage())));
  }

  public Mono<ServerResponse> findAllProductsPaginated(ServerRequest request) {
    final int page = Integer.parseInt(request.queryParam("page").orElse("0"));
    final int size = Integer.parseInt(request.queryParam("size").orElse("5"));
    final Pageable pageable = Pageable.ofSize(size).withPage(page);

    return findPaginatedProducts.execute(pageable)
        .flatMap(productsResultDto -> ServerResponse.ok().bodyValue(toPage(productsResultDto, pageable)))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  private ProductPage toPage(ProductsResultDto productsResultDto, Pageable pageable) {
    final PageImpl<Product> productPageImpl = new PageImpl<>(productsResultDto.products(), pageable,
        productsResultDto.totalProducts());
    return new ProductPage(
        productsResultDto.products(),
        productPageImpl.getNumberOfElements(),
        productPageImpl.getTotalPages(),
        productPageImpl.getNumber(),
        productPageImpl.getSize(),
        productPageImpl.isFirst(),
        productPageImpl.isLast(),
        productPageImpl.isEmpty()
    );
  }
}
