package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.out.jpadata.entity.ShopkeeperEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ShopkeeperOutMapperTest {

    private final ShopkeeperOutMapper mapper = new ShopkeeperOutMapperImpl();

    @Test
    public void testToShopkeeperModelTransformsEntityToModel() {
        ShopkeeperEntity entity = new ShopkeeperEntity();
        entity.setCompanyName("MonCommercant");
        entity.setCategory("ALIMENTATION");
        entity.setSiret("12345678900013");
        entity.setAddress("12 rue de la Republique, 59000 Lille");
        entity.setEmail("moncommercant@example.fr");
        entity.setPassword("hashed-password");
        entity.setStatus(ShopkeeperStatus.WAITING);

        ShopkeeperModel model = mapper.toShopkeeperModel(entity);

        assertThat(model.getEncodedPassword()).isEqualTo("hashed-password");
        assertThat(model.getPassword()).isNull();
        assertThat(model.getCompanyName()).isEqualTo("MonCommercant");
        assertThat(model.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
    }

    @Test
    public void testToShopkeeperEntityTransformsModelToEntity() {
        ShopkeeperModel model = new ShopkeeperModel();
        model.setCompanyName("MonCommercant");
        model.setCategory("ALIMENTATION");
        model.setSiret("12345678900013");
        model.setAddress("12 rue de la Republique, 59000 Lille");
        model.setEmail("moncommercant@example.fr");
        model.setPassword("AStrongPassword123!");
        model.setEncodedPassword("hashed-password");
        model.setStatus(ShopkeeperStatus.WAITING);

        ShopkeeperEntity entity = mapper.toShopkeeperEntity(model);

        assertThat(entity.getPassword()).isEqualTo("hashed-password");
        assertThat(entity.getCompanyName()).isEqualTo("MonCommercant");
        assertThat(entity.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
    }
}
