package org.example.reactive_general_market.src.users.infraestructure.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Table(name = "users")
public class UserEntity {

    @Id
    private UUID id;
    private String name;
    private String password;
    private String email;

    public UserEntity(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
    }
}
