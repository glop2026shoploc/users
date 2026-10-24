package com.glop.users.domain.model;

import lombok.Data;

@Data
public class ShopkeeperModel {

    String companyName;
    String category;
    String siret;
    String address;
    String email;
    String password;
    String encodedPassword;
    ShopkeeperStatus status;
}
