package com.glop.users.domain.port.in;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.model.address.AddressModel;

import java.util.UUID;

public interface ClientPortIn {
    AuthenticationModel register(ClientModel clientModel);
    AuthenticationModel create(UUID userId, ClientModel clientModel);
}
