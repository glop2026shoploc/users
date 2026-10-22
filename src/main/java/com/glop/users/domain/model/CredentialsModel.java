package com.glop.users.domain.model;

import lombok.Data;

@Data
public class CredentialsModel {
    private String email;
    private String password;
    private String encodedPassword;
}
