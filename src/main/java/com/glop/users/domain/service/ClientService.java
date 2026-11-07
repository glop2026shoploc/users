package com.glop.users.domain.service;

import com.glop.users.domain.model.AuthenticationModel;
import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.model.UserModel;
import com.glop.users.domain.model.UserRole;
import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.model.address.CityModel;
import com.glop.users.domain.port.in.ClientPortIn;
import com.glop.users.domain.port.out.ClientPortOut;
import com.glop.users.domain.port.out.ReferentialPortOut;
import com.glop.users.domain.port.out.TokenPortOut;
import com.glop.users.domain.port.out.UserPortOut;
import com.glop.users.exception.credential.ClientAlreadyExistsException;
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
    private final AuthService authService;
    private final ClientPortOut clientPortOut;
    private final ReferentialPortOut referentialPortOut;
    private final TokenPortOut tokenPortOut;
    private final UserPortOut userPortOut;

    @Override
    public AuthenticationModel register(ClientModel clientModel) {
        checkCityExists(clientModel.getCityModel());
        UserModel user = accountService.createAccount(clientModel.getUserModel());
        clientModel.setUserModel(user);
        clientPortOut.create(clientModel);

        return authService.login(user.getUserId(), UserRole.CLIENT);
    }

    private ClientModel createClient(UserModel user, ClientModel clientModel){
        if(clientPortOut.existsByUser(user.getUserId()))throw new ClientAlreadyExistsException("Ce compte est déjà client.");
        clientModel.setUserModel(user);
        return clientPortOut.create(clientModel);
    }

    private void checkCityExists(CityModel cityModel) {
        if (!referentialPortOut.cityExists(cityModel.getId())) {
            throw new UnknownReferenceException("Ville inconnue.");
        }
    }

    @Override
    public AuthenticationModel create(UUID userId, ClientModel clientModel) {
        checkCityExists(clientModel.getCityModel());
        UserModel user = userPortOut.findById(userId).orElseThrow(UserNotFoundException::new);
        createClient(user,clientModel);
        return authService.login(user.getUserId(), UserRole.CLIENT);
    }
}
