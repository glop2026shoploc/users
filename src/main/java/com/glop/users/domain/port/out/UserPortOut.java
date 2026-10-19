package com.glop.users.domain.port.out;

import com.glop.users.domain.model.UserModel;

public interface UserPortOut {
    UserModel findByEmail(String email);

    UserModel create(UserModel userModel);
}
