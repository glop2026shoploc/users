package com.glop.users.domain.service;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.port.in.ShopkeeperPortIn;
import com.glop.users.domain.port.out.ShopkeeperPortOut;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ShopkeeperService implements ShopkeeperPortIn {
    private final PasswordEncoder passwordEncoder;
    private final ShopkeeperPortOut shopkeeperPortOut;

    @Override
    public ShopkeeperModel register(ShopkeeperModel shopkeeperModel) throws BadRequestException {
        Boolean shopkeeperEmailExists = shopkeeperPortOut.findByEmail(shopkeeperModel.getEmail()) != null;
        Boolean shopkeeperSiretExists = shopkeeperPortOut.findBySiret(shopkeeperModel.getSiret()) != null;

        if (shopkeeperEmailExists || shopkeeperSiretExists) throw new BadRequestException("Un commerçant existe déjà avec cet e-mail / ce siret.");

        shopkeeperModel.setEncodedPassword(passwordEncoder.encode(shopkeeperModel.getPassword()));
        shopkeeperModel.setStatut(ShopkeeperStatus.WAITING);

        shopkeeperModel = shopkeeperPortOut.create(shopkeeperModel);

        shopkeeperModel.setPassword(null);
        shopkeeperModel.setEncodedPassword(null);

        return shopkeeperModel;
    }

    @Override
    public List<ShopkeeperModel> getWaitingShopkeepers() {
        return shopkeeperPortOut.findByStatus(ShopkeeperStatus.WAITING);
    }

    @Override
    public ShopkeeperModel updateStatus(Long shopkeeperId, ShopkeeperStatus status) throws BadRequestException {
        if (status == ShopkeeperStatus.WAITING) throw new BadRequestException("Le statut doit être VALIDATED ou REJECTED.");

        ShopkeeperModel shopkeeper = shopkeeperPortOut.findById(shopkeeperId);
        if (shopkeeper == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Commerçant introuvable.");
        if (shopkeeper.getStatus() != ShopkeeperStatus.WAITING) throw new BadRequestException("Ce commerçant a déjà été validé ou refusé.");

        return shopkeeperPortOut.updateStatus(shopkeeperId, status);
    }
}
