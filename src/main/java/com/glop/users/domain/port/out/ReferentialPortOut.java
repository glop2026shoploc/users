package com.glop.users.domain.port.out;

import java.util.UUID;

/** Données de référence (catégories, villes) qui doivent exister avant d'être utilisées. */
public interface ReferentialPortOut {
    boolean categoryExists(UUID categoryId);

    boolean cityExists(UUID cityId);
}
