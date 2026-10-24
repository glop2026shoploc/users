package com.glop.users.out.jpadata.entity;

import com.glop.users.domain.model.CommercantStatus;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "commercants", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"}),
        @UniqueConstraint(columnNames = {"siret"})
})
public class CommercantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="commercant_id")
    Long commercantId;

    @Column(name="company_name")
    private String companyName;

    @Column(name="category")
    private String category;

    @Column(name="siret")
    private String siret;

    @Column(name="adress")
    private String adress;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    private CommercantStatus status;
}
