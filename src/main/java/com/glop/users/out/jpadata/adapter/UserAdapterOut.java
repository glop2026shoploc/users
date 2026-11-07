package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.out.jpadata.entity.UserCredentialsEntity;
import com.glop.users.out.jpadata.mapper.UserOutMapper;
import com.glop.users.out.jpadata.repository.UserCredentialsRepository;
import com.glop.users.out.jpadata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserAdapterOut implements UserPortOut {
    private final UserRepository userRepository;
    private final UserCredentialsRepository credentialsRepository;
    private final UserOutMapper userOutMapper;

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public Optional<UserModel> findById(UUID userId) {
        return userRepository.findById(userId).map(userOutMapper::toUserModel);
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userRepository.findByEmail(email).map(userOutMapper::toUserModel);
    }

    @Override
    public Optional<UserModel> findWithPasswordByEmail(String email) {
        return credentialsRepository.findByEmail(email).map(userOutMapper::toUserModel);
    }

    @Override
    public UserModel create(UserModel userModel) {
        UserCredentialsEntity saved = credentialsRepository.save(userOutMapper.toCredentialsEntity(userModel));
        return userOutMapper.toUserModel(saved.getUser());
    }
}
