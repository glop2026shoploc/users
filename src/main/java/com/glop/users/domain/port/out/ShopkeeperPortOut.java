package com.glop.users.domain.port.out;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;

import java.util.List;

public interface ShopkeeperPortOut {
    ShopkeeperModel findByEmail(String email);
    ShopkeeperModel findBySiret(String siret);
    ShopkeeperModel create(ShopkeeperModel shopkeeperModel);
    List<ShopkeeperModel> findByStatus(ShopkeeperStatus status);
}