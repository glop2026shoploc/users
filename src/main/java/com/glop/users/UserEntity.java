package com.glop.users;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID userId;

    private String email;
    private String name;
    private String password;
    private String role;
}
