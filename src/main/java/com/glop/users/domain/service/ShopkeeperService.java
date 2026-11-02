package com.glop.users.domain.service;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import com.glop.users.domain.port.in.ShopkeeperPortIn;
import com.glop.users.domain.port.out.DocumentPortOut;
import com.glop.users.domain.port.out.ShopkeeperPortOut;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ShopkeeperService implements ShopkeeperPortIn {
    private final PasswordEncoder passwordEncoder;
    private final ShopkeeperPortOut shopkeeperPortOut;
    private final DocumentPortOut documentPortOut;

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

    @Override
    public void uploadDocument(Long shopkeeperId, byte[] content, String contentType) throws BadRequestException {
        ShopkeeperModel shopkeeper = shopkeeperPortOut.findById(shopkeeperId);
        if (shopkeeper == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Commerçant introuvable.");
        if (shopkeeper.getStatus() != ShopkeeperStatus.WAITING) throw new BadRequestException("Ce commerçant a déjà été validé ou refusé.");

        String extension;
        if ("application/pdf".equals(contentType)) {
            extension = ".pdf";
        } else if ("image/jpeg".equals(contentType)) {
            extension = ".jpg";
        } else if ("image/png".equals(contentType)) {
            extension = ".png";
        } else {
            throw new BadRequestException("Le document doit être un PDF, un JPEG ou un PNG.");
        }

        documentPortOut.upload("shopkeepers/" + shopkeeperId + "/" + UUID.randomUUID() + extension, content, contentType);
    }

    @Override
    public List<String> getDocumentUrls(Long shopkeeperId) {
        if (shopkeeperPortOut.findById(shopkeeperId) == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Commerçant introuvable.");

        return documentPortOut.getDownloadUrls("shopkeepers/" + shopkeeperId + "/");
    }
}
