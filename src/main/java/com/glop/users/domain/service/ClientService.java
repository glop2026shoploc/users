package com.glop.users.domain.service;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.port.in.ClientPortIn;
import com.glop.users.domain.port.out.ClientPortOut;
import com.glop.users.domain.port.out.ReferentialPortOut;
import com.glop.users.domain.port.out.TokenPortOut;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.ClientAlreadyExistsException;
import com.glop.users.exception.UnknownReferenceException;
import com.glop.users.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ClientService implements ClientPortIn {
    private final AccountService accountService;
    private final ClientPortOut clientPortOut;
    private final ReferentialPortOut referentialPortOut;
    private final TokenPortOut tokenPortOut;
    private final UserPortOut userPortOut;

    @Override
    public AuthenticationModel register(ClientModel clientModel) {
        checkCityExists(clientModel.getAddressModel());
        UserModel client = accountService.createAccount(clientModel.getUserModel());
        clientModel.setUserModel(client);
        clientPortOut.create(clientModel);

        return AuthenticationModel.bearer(tokenPortOut.generateToken(client));
    }

    private ClientModel createClient(UserModel user, ClientModel clientModel){
        if(clientPortOut.existsByUser(user.getUserId()))throw new ClientAlreadyExistsException("Ce compte est déjà client.");
        clientModel.setUserModel(user);
        return clientPortOut.create(clientModel);
    }

    private void checkCityExists(AddressModel address) {
        if (!referentialPortOut.cityExists(address.getCityModel().getId())) {
            throw new UnknownReferenceException("Ville inconnue.");
        }
    }

    @Override
    public AuthenticationModel create(UUID userId, ClientModel clientModel) {
        UserModel user = userPortOut.findById(userId).orElseThrow(UserNotFoundException::new);
        return AuthenticationModel.bearer(tokenPortOut.generateToken(createClient(user,clientModel).getUserModel()));
    }
}
