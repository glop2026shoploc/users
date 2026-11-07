package com.glop.users.domain.port.out;

import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.model.address.AddressModel;

import java.util.UUID;

public interface ClientPortOut {
    /** Crée l'adresse puis la fiche client de l'utilisateur. */
    ClientModel create(ClientModel clientModel);

    boolean existsByUser(UUID userId);
}
