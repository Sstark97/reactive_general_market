package org.example.reactive_general_market.src.users.infraestructure;

import org.example.reactive_general_market.src.shared.containers.ReactivePostgresContainer;
import org.example.reactive_general_market.src.users.infraestructure.models.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
@Import(ReactivePostgresContainer.class)
class UserControllerTest {

    @Autowired
    private WebTestClient webClient;

    @Test
    void should_create_a_new_user_given_user_information() {
        var user = new UserRequest("test", "test@test.com", "1234");

        webClient.post()
                .uri("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .exchange()
                .expectStatus()
                .isCreated();
    }
}