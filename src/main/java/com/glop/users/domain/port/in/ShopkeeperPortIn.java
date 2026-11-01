package com.glop.users.domain.port.in;

import com.glop.users.domain.model.ShopkeeperModel;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ShopkeeperPortIn {
    ShopkeeperModel register(ShopkeeperModel shopkeeperModel) throws BadRequestException;
    List<ShopkeeperModel> getWaitingShopkeepers();
}
