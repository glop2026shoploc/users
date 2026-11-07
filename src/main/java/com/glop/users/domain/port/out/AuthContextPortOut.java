package com.glop.users.domain.port.out;

import com.glop.users.domain.model.AuthContext;

import java.util.Optional;
import java.util.UUID;

public interface AuthContextPortOut {
    Optional<AuthContext> getAdminContext(UUID userId);
    Optional<AuthContext> getMerchantContext(UUID userId);
    Optional<AuthContext> getClientContext(UUID userId);
}
