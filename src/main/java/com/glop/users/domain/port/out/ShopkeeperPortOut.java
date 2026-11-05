package com.glop.users.domain.port.out;

import com.glop.users.domain.model.ShopkeeperModel;

public interface ShopkeeperPortOut {
    ShopkeeperModel findByEmail(String email);
    ShopkeeperModel findBySiret(String siret);
    ShopkeeperModel create(ShopkeeperModel shopkeeperModel);
}