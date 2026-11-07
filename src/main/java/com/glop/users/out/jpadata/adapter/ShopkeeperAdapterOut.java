package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.port.out.ShopkeeperPortOut;
import com.glop.users.out.jpadata.entity.EmployeeEntity;
import com.glop.users.out.jpadata.entity.EmployeeRole;
import com.glop.users.out.jpadata.entity.ShopEntity;
import com.glop.users.out.jpadata.mapper.ShopkeeperOutMapper;
import com.glop.users.out.jpadata.repository.CategoryRepository;
import com.glop.users.out.jpadata.repository.EmployeeRepository;
import com.glop.users.out.jpadata.repository.ShopRepository;
import com.glop.users.out.jpadata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ShopkeeperAdapterOut implements ShopkeeperPortOut {
    private final ShopRepository shopRepository;
    private final EmployeeRepository employeeRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final AddressWriter addressWriter;
    private final ShopkeeperOutMapper shopkeeperOutMapper;

    @Override
    public boolean existsBySiret(String siret) {
        return shopRepository.existsBySiretNumber(siret);
    }

    @Override
    public boolean existsByOwner(UUID userId) {
        return employeeRepository.existsByUser_UserId(userId);
    }

    @Override
    public ShopkeeperModel create(ShopkeeperModel shopkeeperModel) {
        ShopEntity shop = shopRepository.save(toNewShop(shopkeeperModel));
        employeeRepository.save(toOwner(shopkeeperModel.getOwner().getUserId(), shop));

        ShopkeeperModel created = shopkeeperOutMapper.toShopkeeperModel(shop);
        created.setOwner(shopkeeperModel.getOwner());
        return created;
    }

    private ShopEntity toNewShop(ShopkeeperModel shopkeeperModel) {
        ShopEntity shop = shopkeeperOutMapper.toShopEntity(shopkeeperModel);
        if(shopkeeperModel.getCategoryId()!=null){
            shop.setCategory(categoryRepository.findById(shopkeeperModel.getCategoryId()).get());
        }
        shop.setAddress(addressWriter.save(shopkeeperModel.getAddress()));
        return shop;
    }

    private EmployeeEntity toOwner(UUID userId, ShopEntity shop) {
        EmployeeEntity owner = new EmployeeEntity();
        owner.setUser(userRepository.getReferenceById(userId));
        owner.setShop(shop);
        owner.setRole(EmployeeRole.OWNER);
        return owner;
    }
}
