package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.port.out.ShopkeeperPortOut;
import com.glop.users.out.jpadata.entity.EmployeeEntity;
import com.glop.users.out.jpadata.entity.EmployeeRole;
import com.glop.users.out.jpadata.repository.EmployeeRepository;
import com.glop.users.out.jpadata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShopkeeperAdapterOut implements ShopkeeperPortOut {
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;

    @Override
    public boolean existsByOwner(UUID userId) {
        return employeeRepository.existsByUser_UserId(userId);
    }

    @Override
    public void addOwner(UUID userId, UUID shopId) {
        EmployeeEntity owner = new EmployeeEntity();
        owner.setUser(userRepository.getReferenceById(userId));
        owner.setShopId(shopId);
        owner.setRole(EmployeeRole.OWNER);
        employeeRepository.save(owner);
    }
}
