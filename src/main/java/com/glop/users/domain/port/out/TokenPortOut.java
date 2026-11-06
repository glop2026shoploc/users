package com.glop.users.domain.port.out;

import com.glop.users.domain.model.UserModel;

public interface TokenPortOut {
    String generateToken(UserModel userModel);
}
