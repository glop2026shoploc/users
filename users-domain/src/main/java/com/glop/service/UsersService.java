package com.glop.service;

import com.glop.model.UsersModel;
import com.glop.repository.IUsersRepository;

import java.util.UUID;

public class UsersService implements IUsersService {
    IUsersRepository usersRepository;
    @Override
    public UsersModel findCurrentUsers(UUID keycloakId) {
        return usersRepository.findUsersByKeycloakId(keycloakId);
    }
}
