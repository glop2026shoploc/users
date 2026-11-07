package com.glop.users.out.jpadata.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "address")
@Getter
@Setter
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "address_id")
    private UUID addressId;

    @Column(name = "road", nullable = false)
    private String road;

    /** Texte et non nombre : "12 bis", "3B"... */
    @Column(name = "number", length = 20)
    private String number;

    @Column(name = "complement")
    private String complement;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "city_id", nullable = false)
    private CityEntity city;
}
