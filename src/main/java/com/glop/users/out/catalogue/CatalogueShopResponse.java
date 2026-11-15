package com.glop.users.out.catalogue;

import java.util.List;
import java.util.UUID;

/** POST {catalogue}/shops. */
record CatalogueShopResponse(UUID shopId, String name, String siret, List<UUID> categoryIds,
                             Address address, String status) {
    record Address(UUID addressId, String road, String number, String complement, UUID cityId) {
    }
}
