package org.example.reactive_general_market.src.product.infrastructure.mapper;

import java.util.UUID;

import org.example.reactive_general_market.src.product.application.dto.ProductDto;
import org.example.reactive_general_market.src.product.application.dto.ProductPage;
import org.example.reactive_general_market.src.product.application.dto.ProductsResultDto;
import org.example.reactive_general_market.src.product.application.dto.UpdatedProductDto;
import org.example.reactive_general_market.src.product.domain.model.Product;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class ProductMapper {
  private ProductMapper() {
    throw new IllegalStateException("Utility class");
  }
  public static ProductPage toPage(ProductsResultDto productsResultDto, Pageable pageable) {
    final PageImpl<Product> productPageImpl = new PageImpl<>(productsResultDto.products(), pageable,
        productsResultDto.totalProducts());
    return new ProductPage(
        productsResultDto.products(),
        productsResultDto.totalProducts().intValue(),
        productPageImpl.getTotalPages(),
        productPageImpl.getNumber(),
        productPageImpl.getSize(),
        productPageImpl.isFirst(),
        productPageImpl.isLast(),
        productPageImpl.isEmpty()
    );
  }

  public static UpdatedProductDto toUpdatedProductDto(ProductDto createdProductDto,
      UUID productId) {
    return new UpdatedProductDto(
        productId,
        createdProductDto.name(),
        createdProductDto.description(),
        createdProductDto.price()
    );
  }
}
