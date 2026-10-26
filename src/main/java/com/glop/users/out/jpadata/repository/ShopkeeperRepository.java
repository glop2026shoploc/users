package com.glop.users.out.jpadata.repository;

import com.glop.users.out.jpadata.entity.ShopkeeperEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopkeeperRepository extends JpaRepository<ShopkeeperEntity, Long> {
    ShopkeeperEntity findByEmail(String email);
    ShopkeeperEntity findBySiret(String siret);
}
