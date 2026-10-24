package com.glop.users.out.jpadata.repository;

import com.glop.users.out.jpadata.entity.CommercantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommercantRepository extends JpaRepository<CommercantEntity, Long> {
    CommercantEntity findByEmail(String email);
    CommercantEntity findBySiret(String siret);
}
