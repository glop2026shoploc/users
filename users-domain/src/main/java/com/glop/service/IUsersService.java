package com.glop.service;

import com.glop.model.UsersModel;

import java.util.UUID;

public interface IUsersService {
    UsersModel findCurrentUsers(UUID keycloakId);
}
