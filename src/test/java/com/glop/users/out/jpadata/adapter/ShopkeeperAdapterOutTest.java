package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.out.jpadata.entity.ShopkeeperEntity;
import com.glop.users.out.jpadata.mapper.ShopkeeperOutMapperImpl;
import com.glop.users.out.jpadata.repository.ShopkeeperRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ShopkeeperAdapterOutTest {
    @Mock
    private ShopkeeperRepository shopkeeperRepository;

    private ShopkeeperAdapterOut shopkeeperAdapterOut;

    private ShopkeeperEntity entity;


    @BeforeEach
    void setUp() {
        shopkeeperAdapterOut = new ShopkeeperAdapterOut(shopkeeperRepository, new ShopkeeperOutMapperImpl());
        this.entity = new ShopkeeperEntity();
        entity.setEmail("moncommercant@example.com");
        entity.setSiret("12345678900013");
        entity.setPassword("hashed-password");
    }

    @Test
    void testFindByEmailShouldReturnMappedShopkeeper() {
        when(shopkeeperRepository.findByEmail("moncommercant@example.com")).thenReturn(entity);

        ShopkeeperModel result = shopkeeperAdapterOut.findByEmail("moncommercant@example.com");

        assertThat(result.getEmail()).isEqualTo("moncommercant@example.com");
    }

    @Test
    void testFindBySiretShouldReturnMappedShopkeeper() {
        when(shopkeeperRepository.findBySiret("12345678900013")).thenReturn(entity);

        ShopkeeperModel result = shopkeeperAdapterOut.findBySiret("12345678900013");

        assertThat(result.getSiret()).isEqualTo("12345678900013");
    }

    @Test
    void testCreateShouldReturnMappedShopkeeperAndHashedPassword() {
        when(shopkeeperRepository.save(any())).thenReturn(entity);

        ShopkeeperModel result = shopkeeperAdapterOut.create(new ShopkeeperModel());

        assertThat(result.getEncodedPassword()).isEqualTo("hashed-password");
    }

    @Test
    void testFindByStatusShouldReturnMappedShopkeepers() {
        entity.setShopkeeperId(1L);
        when(shopkeeperRepository.findByStatus(ShopkeeperStatus.WAITING)).thenReturn(List.of(entity));

        List<ShopkeeperModel> result = shopkeeperAdapterOut.findByStatus(ShopkeeperStatus.WAITING);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getShopkeeperId()).isEqualTo(1L);
    }
}
