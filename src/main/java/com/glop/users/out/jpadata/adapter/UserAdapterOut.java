package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.out.jpadata.mapper.UserOutMapper;
import com.glop.users.out.jpadata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAdapterOut implements UserPortOut {
    private final UserRepository userRepository;
    private final UserOutMapper userOutMapper;

    @Override
    public UserModel findByEmail(String email) {
        return userOutMapper.toUserModel(userRepository.findByEmail(email));
    }

    @Override
    public UserModel create(UserModel userModel) {
        userRepository.save(userOutMapper.toUserEntity(userModel));

        return userOutMapper.toUserModel(userRepository.save(userOutMapper.toUserEntity(userModel)));
    }
}
