package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.out.jpadata.entity.AddressEntity;
import com.glop.users.out.jpadata.entity.CategoryEntity;
import com.glop.users.out.jpadata.entity.ShopEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShopkeeperOutMapperTest {

    @Mock
    private AddressOutMapper addressOutMapper;

    @InjectMocks
    private ShopkeeperOutMapper mapper;

    @Test
    void testToShopkeeperModelTransformsEntityToModel() {
        UUID shopId = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategoryId(categoryId);

        // Le contenu de l'adresse n'a pas d'importance : son mapping est testé dans AddressOutMapper
        AddressEntity addressEntity = new AddressEntity();
        AddressModel addressModel = new AddressModel();
        when(addressOutMapper.toAddressModel(addressEntity)).thenReturn(addressModel);

        ShopEntity shopEntity = new ShopEntity();
        shopEntity.setShopId(shopId);
        shopEntity.setName("MonCommercant");
        shopEntity.setSiretNumber("12345678900013");
        shopEntity.setCategory(categoryEntity);
        shopEntity.setAddress(addressEntity);
        shopEntity.setStatus(ShopkeeperStatus.WAITING);

        ShopkeeperModel model = mapper.toShopkeeperModel(shopEntity);

        verify(addressOutMapper).toAddressModel(addressEntity);
        assertThat(model.getAddress()).isSameAs(addressModel);

        assertThat(model.getShopId()).isEqualTo(shopId);
        assertThat(model.getCompanyName()).isEqualTo("MonCommercant");
        assertThat(model.getSiret()).isEqualTo("12345678900013");
        assertThat(model.getCategoryId()).isEqualTo(categoryId);
        assertThat(model.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
        assertThat(model.getOwner()).isNull();
    }

    @Test
    void testToShopEntityIgnoresCategoryAndAddress() {
        ShopkeeperModel model = new ShopkeeperModel();
        model.setCompanyName("MonCommercant");
        model.setCategoryId(UUID.randomUUID());
        model.setSiret("12345678900013");
        model.setAddress(new AddressModel());
        model.setStatus(ShopkeeperStatus.WAITING);

        ShopEntity entity = mapper.toShopEntity(model);

        assertThat(entity.getName()).isEqualTo("MonCommercant");
        assertThat(entity.getSiretNumber()).isEqualTo("12345678900013");
        assertThat(entity.getStatus()).isEqualTo(ShopkeeperStatus.WAITING);
        assertThat(entity.getShopId()).isNull();
        assertThat(entity.getCategory()).isNull();
        assertThat(entity.getAddress()).isNull();
    }
}