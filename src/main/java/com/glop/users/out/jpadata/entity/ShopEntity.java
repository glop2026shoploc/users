package com.glop.users.out.jpadata.entity;

import com.glop.users.domain.model.ShopkeeperStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.UUID;

@Entity
@Table(name = "shop")
@Getter
@Setter
public class ShopEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "shop_id")
    private UUID shopId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    /** char(14) en base, 14 chiffres (contrainte CHECK côté SQL). */
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(name = "siret_number", nullable = false, unique = true, length = 14)
    private String siretNumber;

    /** 0,1 : un magasin peut ne pas avoir de catégorie. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private CategoryEntity category;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "address_id", nullable = false, unique = true)
    private AddressEntity address;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ShopkeeperStatus status;
}
