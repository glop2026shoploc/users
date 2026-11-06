package com.glop.users.domain.model;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class UserModel {
    UUID userId;
    String email;
    String firstName;
    String lastName;
    String password;
    String encodedPassword;
}
