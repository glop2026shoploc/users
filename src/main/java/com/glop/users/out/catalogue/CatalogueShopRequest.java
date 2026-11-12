package com.glop.users.out.catalogue;

import java.util.List;
import java.util.UUID;

/** POST {catalogue}/shops. */
record CatalogueShopRequest(String name, String siret, List<UUID> categoryIds, Address address) {
    record Address(String road, String number, String complement, UUID cityId) {
    }
}
