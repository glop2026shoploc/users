package com.glop.users.domain.port.out;

import com.glop.users.domain.model.AuthContext;

public interface TokenPortOut {
    String generateToken(AuthContext authContext);
}
