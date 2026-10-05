package com.glop.repository;

import com.glop.model.UsersModel;

import java.util.UUID;

public interface IUsersRepository {
    UsersModel findUsersByKeycloakId(UUID keycloakId);
}
