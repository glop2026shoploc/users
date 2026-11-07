package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.AuthContext;
import com.glop.users.domain.port.out.AuthContextPortOut;
import com.glop.users.out.jpadata.mapper.AuthContextOutMapper;
import com.glop.users.out.jpadata.repository.ClientRepository;
import com.glop.users.out.jpadata.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;
@Component
@RequiredArgsConstructor
public class AuthContextAdapterOut implements AuthContextPortOut {
    private final AuthContextOutMapper authContextOutMapper;
    private final ClientRepository clientRepository;
    private final EmployeeRepository employeeRepository;


    @Override
    public Optional<AuthContext> getAdminContext(UUID userId) {
        return Optional.empty();
    }

    @Override
    public Optional<AuthContext> getMerchantContext(UUID userId) {
        return employeeRepository.findOptionalByUser_UserId(userId)
                .map(authContextOutMapper::toAuthContext);
    }

    @Override
    public Optional<AuthContext> getClientContext(UUID userId) {
        return clientRepository.findOptionalByUser_UserId(userId)
                .map(authContextOutMapper::toAuthContext);
    }
}
