package com.glop.users.domain.port.in;

import com.glop.users.domain.model.ShopkeeperModel;
import com.glop.users.domain.model.ShopkeeperStatus;
import org.apache.coyote.BadRequestException;

import java.util.List;

public interface ShopkeeperPortIn {
    ShopkeeperModel register(ShopkeeperModel shopkeeperModel) throws BadRequestException;
    List<ShopkeeperModel> getWaitingShopkeepers();
    ShopkeeperModel updateStatus(Long shopkeeperId, ShopkeeperStatus status) throws BadRequestException;
    void uploadDocument(Long shopkeeperId, byte[] content, String contentType) throws BadRequestException;
    List<String> getDocumentUrls(Long shopkeeperId);
}
