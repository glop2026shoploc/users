package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.UserModel;
import com.glop.users.out.jpadata.entity.UserCredentialsEntity;
import com.glop.users.out.jpadata.entity.UserEntity;
import com.glop.users.out.jpadata.mapper.UserOutMapper;
import com.glop.users.out.jpadata.repository.UserCredentialsRepository;
import com.glop.users.out.jpadata.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserAdapterOutTest {
    private static final String EMAIL = "test@glop.fr";

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserCredentialsRepository credentialsRepository;
    @Mock
    private UserOutMapper userOutMapper;
    @InjectMocks
    private UserAdapterOut adapter;

    private final UserEntity entity = new UserEntity();
    private final UserModel model = new UserModel();

    @Test
    void testExistsByEmail() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));

        assertThat(adapter.existsByEmail(EMAIL)).isTrue();
    }

    @Test
    void testFindByIdAndFindByEmailShouldMapUser() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(entity));
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(entity));
        when(userOutMapper.toUserModel(entity)).thenReturn(model);

        assertThat(adapter.findById(id)).contains(model);
        assertThat(adapter.findByEmail(EMAIL)).contains(model);
    }

    @Test
    void testFindWithPasswordByEmailShouldMapCredentials() {
        UserCredentialsEntity credentials = new UserCredentialsEntity();
        when(credentialsRepository.findByEmail(EMAIL)).thenReturn(Optional.of(credentials));
        when(userOutMapper.toUserModel(credentials)).thenReturn(model);

        assertThat(adapter.findWithPasswordByEmail(EMAIL)).contains(model);
    }

    @Test
    void testCreateShouldSaveCredentialsAndReturnUser() {
        UserCredentialsEntity credentials = new UserCredentialsEntity();
        credentials.setUser(entity);
        when(userOutMapper.toCredentialsEntity(model)).thenReturn(credentials);
        when(credentialsRepository.save(credentials)).thenReturn(credentials);
        when(userOutMapper.toUserModel(entity)).thenReturn(model);

        assertThat(adapter.create(model)).isEqualTo(model);
    }
}
