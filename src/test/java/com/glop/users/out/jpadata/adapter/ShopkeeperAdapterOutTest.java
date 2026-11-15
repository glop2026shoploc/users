package com.glop.users.out.jpadata.adapter;

import com.glop.users.out.jpadata.entity.EmployeeEntity;
import com.glop.users.out.jpadata.entity.EmployeeRole;
import com.glop.users.out.jpadata.entity.UserEntity;
import com.glop.users.out.jpadata.repository.EmployeeRepository;
import com.glop.users.out.jpadata.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShopkeeperAdapterOutTest {
    private static final UUID USER_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");
    private static final UUID SHOP_ID = UUID.fromString("00000000-0000-0000-0000-000000000002");

    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private ShopkeeperAdapterOut shopkeeperAdapterOut;

    @Test
    void testExistsByOwnerShouldDelegateToEmployeeRepository() {
        when(employeeRepository.existsByUser_UserId(USER_ID)).thenReturn(true);

        assertThat(shopkeeperAdapterOut.existsByOwner(USER_ID)).isTrue();
    }

    @Test
    void testAddOwnerShouldSaveOwnerEmployeeWithShopId() {
        UserEntity user = new UserEntity();
        user.setUserId(USER_ID);
        when(userRepository.getReferenceById(USER_ID)).thenReturn(user);

        shopkeeperAdapterOut.addOwner(USER_ID, SHOP_ID);

        ArgumentCaptor<EmployeeEntity> captor = ArgumentCaptor.forClass(EmployeeEntity.class);
        verify(employeeRepository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isEqualTo(user);
        assertThat(captor.getValue().getShopId()).isEqualTo(SHOP_ID);
        assertThat(captor.getValue().getRole()).isEqualTo(EmployeeRole.OWNER);
    }
}
