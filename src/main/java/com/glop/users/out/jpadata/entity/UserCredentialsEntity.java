package com.glop.users.out.jpadata.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "credentials")
@Getter
@Setter
public class UserCredentialsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "credential_id")
    private UUID credentialId;

    /** PERSIST : enregistrer les credentials d'un nouveau compte enregistre aussi le user. */
    @OneToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    /** Hash bcrypt/argon2, jamais le mot de passe en clair. */
    @Column(name = "password", nullable = false)
    private String password;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;
}
