package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.out.jpadata.entity.CityEntity;
import com.glop.users.out.jpadata.entity.ClientEntity;
import com.glop.users.out.jpadata.entity.UserEntity;
import com.glop.users.out.jpadata.mapper.ClientOutMapper;
import com.glop.users.out.jpadata.repository.CityRepository;
import com.glop.users.out.jpadata.repository.ClientRepository;
import com.glop.users.out.jpadata.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientAdapterOutTest {
    private static final UUID USER_ID = UUID.randomUUID();
    private static final UUID CITY_ID = UUID.randomUUID();

    @Mock
    private ClientRepository clientRepository;
    @Mock
    private ClientOutMapper clientOutMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CityRepository cityRepository;
    @InjectMocks
    private ClientAdapterOut adapter;

    @Test
    void testCreateShouldSaveClientWithUserAndCity() {
        UserEntity user = new UserEntity();
        CityEntity city = new CityEntity();
        ClientModel expected = new ClientModel();
        when(userRepository.getReferenceById(USER_ID)).thenReturn(user);
        when(cityRepository.getReferenceById(CITY_ID)).thenReturn(city);
        when(clientRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(clientOutMapper.toClientModel(any())).thenReturn(expected);
        UserModel userModel = new UserModel();
        userModel.setUserId(USER_ID);
        ClientModel client = ClientModel.builder().userModel(userModel).cityModel(CityModel.builder().id(CITY_ID).build()).build();

        assertThat(adapter.create(client)).isEqualTo(expected);

        ArgumentCaptor<ClientEntity> captor = ArgumentCaptor.forClass(ClientEntity.class);
        verify(clientRepository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getCity()).isEqualTo(city);
    }

    @Test
    void testExistsByUserShouldReturnTrueWhenClientFound() {
        when(clientRepository.findByUser_userId(USER_ID)).thenReturn(Optional.of(new ClientEntity()));

        assertThat(adapter.existsByUser(USER_ID)).isTrue();
    }
}
