package com.glop.users.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class UserModel {
    String email;
    String firstName;
    String lastName;
    String password;
    String encodedPassword;
    boolean dataUsageConsent;
    List<String> roles;
}
