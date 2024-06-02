package org.example.reactive_general_market.src.product.infrastructure.handler;

import java.util.UUID;

import org.example.reactive_general_market.src.product.application.CreateProduct;
import org.example.reactive_general_market.src.product.application.FindPaginatedProducts;
import org.example.reactive_general_market.src.product.application.UpdateProduct;
import org.example.reactive_general_market.src.product.application.dto.ProductDto;
import org.example.reactive_general_market.src.product.infrastructure.mapper.ProductMapper;
import org.example.reactive_general_market.src.product.infrastructure.model.ErrorResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {
  private final CreateProduct createProduct;
  private final FindPaginatedProducts findPaginatedProducts;
  private final UpdateProduct updateProduct;

  public ProductHandler(CreateProduct createProduct, FindPaginatedProducts findPaginatedProducts,
      UpdateProduct updateProduct) {
    this.createProduct = createProduct;
    this.findPaginatedProducts = findPaginatedProducts;
    this.updateProduct = updateProduct;
  }

  public Mono<ServerResponse> createProduct(ServerRequest request) {
    return request.bodyToMono(ProductDto.class)
      .flatMap(createProduct::execute)
      .flatMap(createdProduct -> ServerResponse.ok().bodyValue(createdProduct))
        .onErrorResume(error -> ServerResponse.badRequest().bodyValue(new ErrorResponse(error.getMessage())));
  }

  public Mono<ServerResponse> findAllProductsPaginated(ServerRequest request) {
    final int page = Integer.parseInt(request.queryParam("page").orElse("0"));
    final int size = Integer.parseInt(request.queryParam("size").orElse("5"));
    final Pageable pageable = Pageable.ofSize(size).withPage(page);

    return findPaginatedProducts.execute(pageable)
        .flatMap(productsResultDto ->
            ServerResponse.ok().bodyValue(ProductMapper.toPage(productsResultDto, pageable))
        )
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> updateProduct(ServerRequest request) {
    final var productId = UUID.fromString(request.pathVariable("id"));

    return request.bodyToMono(ProductDto.class)
        .map(createdProductDto -> ProductMapper.toUpdatedProductDto(createdProductDto, productId))
        .flatMap(updateProduct::execute)
        .flatMap(updatedProduct -> ServerResponse.ok().bodyValue(updatedProduct))
        .switchIfEmpty(ServerResponse.notFound().build())
        .onErrorResume(error ->
            ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(new ErrorResponse(error.getMessage()))
        );
  }
}
