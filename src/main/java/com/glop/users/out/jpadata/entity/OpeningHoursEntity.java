package com.glop.users.out.jpadata.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.UUID;

@Entity
@Table(name = "opening_hours",
        uniqueConstraints = @UniqueConstraint(columnNames = {"shop_id", "day_of_week", "open_time"}))
@Getter
@Setter
public class OpeningHoursEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "opening_hours_id")
    private UUID openingHoursId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    /** 1 = lundi … 7 = dimanche (ISO, comme java.time.DayOfWeek#getValue). */
    @Column(name = "day_of_week", nullable = false)
    private Short dayOfWeek;

    @Column(name = "open_time", nullable = false)
    private LocalTime openTime;

    @Column(name = "close_time", nullable = false)
    private LocalTime closeTime;
}
