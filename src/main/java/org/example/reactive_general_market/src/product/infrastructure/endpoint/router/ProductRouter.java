package org.example.reactive_general_market.src.product.infrastructure.endpoint.router;

import org.example.reactive_general_market.src.product.infrastructure.handler.ProductHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ProductRouter {
  private final ProductHandler productHandler;

  public ProductRouter(ProductHandler productHandler) {
    this.productHandler = productHandler;
  }

  @Bean
  public RouterFunction<ServerResponse> routes() {
    return RouterFunctions
        .nest(RequestPredicates.path("general_market/api/v1"), productRoutes());
  }

  private RouterFunction<ServerResponse> productRoutes() {
    return RouterFunctions
        .route(RequestPredicates.POST("products"), productHandler::createProduct)
        .andRoute(RequestPredicates.GET("products/all"), productHandler::findAllProductsPaginated)
        .andRoute(RequestPredicates.PUT("products/{id}"), productHandler::updateProduct)
        .andRoute(RequestPredicates.DELETE("products/{id}"), productHandler::deleteProduct);
  }
}
