package com.glop.users.domain.model;

import com.glop.users.domain.model.address.AddressModel;
import lombok.Data;

import java.util.UUID;

@Data
public class ShopkeeperModel {
    private UUID shopId;
    private UserModel owner;

    private String companyName;
    private String siret;
    private UUID categoryId;
    private AddressModel address;

    private ShopkeeperStatus status;
}
