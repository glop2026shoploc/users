package com.glop.users.out.jpadata.repository;

import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.out.jpadata.entity.ShopkeeperEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShopkeeperRepository extends JpaRepository<ShopkeeperEntity, Long> {
    ShopkeeperEntity findByEmail(String email);
    ShopkeeperEntity findBySiret(String siret);
    List<ShopkeeperEntity> findByStatus(ShopkeeperStatus status);
    ShopkeeperEntity findByShopkeeperId(Long shopkeeperId);
}
