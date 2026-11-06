package com.glop.users.out.jpadata.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "city",
        uniqueConstraints = @UniqueConstraint(columnNames = {"country_id", "zip_code", "name"}))
@Getter
@Setter
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "city_id")
    private UUID cityId;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "zip_code", nullable = false, length = 10)
    private String zipCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private CountryEntity country;
}
