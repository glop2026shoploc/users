package com.glop.users.out.jpadata.repository;

import com.glop.users.out.jpadata.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, UUID> {
    boolean existsByUser_UserId(UUID userId);
}
