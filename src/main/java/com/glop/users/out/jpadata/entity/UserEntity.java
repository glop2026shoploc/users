package com.glop.users.out.jpadata.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "users",uniqueConstraints = @UniqueConstraint(columnNames = {"email"}))
@Data
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID userId;
    private String email;
    private String lastName;
    private String firstName;
    private String password;
    private String role;
    private boolean dataUsageConsent;
}
