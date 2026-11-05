package com.glop.users.out.jpadata.entity;

import com.glop.users.domain.model.ShopkeeperStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "shopkeepers", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"siret"})
})
public class ShopkeeperEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="shopkeeper_id")
    Long shopkeeperId;

    @Column(name="company_name")
    private String companyName;

    @Column(name="category")
    private String category;

    @Column(name="siret")
    private String siret;

    @Column(name="address")
    private String address;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private ShopkeeperStatus status;
}
