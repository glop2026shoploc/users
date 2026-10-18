package com.glop.users.out.jpadata.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "users_security")
@Data
public class UserSecurityEntity {
    @Id
    @OneToOne()
    private UserEntity userId;
    private String password;
}
