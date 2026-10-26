package com.glop.users.domain.port.in;

import com.glop.users.domain.model.ShopkeeperModel;
import org.apache.coyote.BadRequestException;

public interface ShopkeeperPortIn {
    ShopkeeperModel register(ShopkeeperModel shopkeeperModel) throws BadRequestException;
}
