package com.glop.users.out.jpadata.repository;

import com.glop.users.out.jpadata.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CityRepository extends JpaRepository<CityEntity, UUID> {
}
