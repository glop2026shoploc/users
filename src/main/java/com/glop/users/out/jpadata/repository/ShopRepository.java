package com.glop.users.out.jpadata.repository;

import com.glop.users.out.jpadata.entity.ShopEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShopRepository extends JpaRepository<ShopEntity, UUID> {
    boolean existsBySiretNumber(String siretNumber);
}
