package com.glop.users.out.jpadata.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "country")
@Getter
@Setter
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "country_id")
    private UUID countryId;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
}
