package org.example.reactive_general_market;

import org.example.reactive_general_market.infrastructure.containers.ReactivePostgresContainer;
import org.springframework.boot.SpringApplication;

public class LocalReactiveGeneralMarketApplication {
  public static void main(String[] args) {
    SpringApplication.from(ReactiveGeneralMarketApplication::main)
        .with(ReactivePostgresContainer.class)
        .run(args);
  }
}
