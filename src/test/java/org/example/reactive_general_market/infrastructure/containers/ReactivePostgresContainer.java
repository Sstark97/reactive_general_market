package org.example.reactive_general_market.infrastructure.containers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration(proxyBeanMethods = false)
public class ReactivePostgresContainer {
  @Bean
  @ServiceConnection
  public PostgreSQLContainer postgreSQLContainer() {
    return new PostgreSQLContainer<>("postgres:13.2");
  }
}
