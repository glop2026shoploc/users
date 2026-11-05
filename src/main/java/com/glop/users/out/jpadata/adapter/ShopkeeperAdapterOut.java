package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.port.out.ShopkeeperPortOut;
import com.glop.users.out.jpadata.entity.ShopkeeperEntity;
import com.glop.users.out.jpadata.mapper.ShopkeeperOutMapper;
import com.glop.users.out.jpadata.repository.ShopkeeperRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShopkeeperAdapterOut implements ShopkeeperPortOut {
    private final ShopkeeperRepository shopkeeperRepository;
    private final ShopkeeperOutMapper shopkeeperOutMapper;


    @Override
    public ShopkeeperModel findByEmail(String email) {
        return shopkeeperOutMapper.toShopkeeperModel(shopkeeperRepository.findByEmail(email));
    }

    @Override
    public ShopkeeperModel findBySiret(String siret) {
        return shopkeeperOutMapper.toShopkeeperModel(shopkeeperRepository.findBySiret(siret));
    }

    @Override
    public ShopkeeperModel create(ShopkeeperModel shopkeeperModel) {
        ShopkeeperEntity shopkeeperEntity = shopkeeperOutMapper.toShopkeeperEntity(shopkeeperModel);
        ShopkeeperEntity shopkeeperCreated = shopkeeperRepository.save(shopkeeperEntity);
        return shopkeeperOutMapper.toShopkeeperModel(shopkeeperCreated);
    }
}
