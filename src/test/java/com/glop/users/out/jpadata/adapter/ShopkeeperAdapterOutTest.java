package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.out.jpadata.entity.EmployeeEntity;
import com.glop.users.out.jpadata.entity.ShopEntity;
import com.glop.users.out.jpadata.entity.UserEntity;
import com.glop.users.out.jpadata.mapper.ShopkeeperOutMapper;
import com.glop.users.out.jpadata.mapper.ShopkeeperOutMapperImpl;
import com.glop.users.out.jpadata.repository.CategoryRepository;
import com.glop.users.out.jpadata.repository.EmployeeRepository;
import com.glop.users.out.jpadata.repository.ShopRepository;
import com.glop.users.out.jpadata.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShopkeeperAdapterOutTest {
    @Mock
    private ShopRepository shopRepository;
    @Mock
    private EmployeeRepository employeeRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private AddressWriter addressWriter;
    @Mock
    private ShopkeeperOutMapper shopkeeperOutMapper;
    @InjectMocks
    private ShopkeeperAdapterOut shopkeeperAdapterOut;

    private ShopEntity shop;
    private EmployeeEntity employeeEntity;
    private UserEntity user;

    @BeforeEach
    void setUp() {
        this.shop = new ShopEntity();
        this.user = new UserEntity();
        user.setUserId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        shop.setSiretNumber("12345678900013");
    }

    @Test
    void testExistsByUserIdShouldReturnMappedShopkeeper() {
        when(employeeRepository.existsByUser_UserId(user.getUserId())).thenReturn(true);

        assertThat(shopkeeperAdapterOut.existsByOwner(user.getUserId())).isTrue();
    }

    @Test
    void testExistsBySiretNumberShouldReturnTrueIfExist() {
        when(shopRepository.existsBySiretNumber("12345678900013")).thenReturn(true);

        assertThat(shopkeeperAdapterOut.existsBySiret("12345678900013")).isTrue();

    }
}
