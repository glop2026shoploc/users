package com.glop.users.out.jpadata.adapter;

import com.glop.users.domain.model.ClientModel;
import com.glop.users.domain.model.address.AddressModel;
import com.glop.users.domain.port.out.ClientPortOut;
import com.glop.users.out.jpadata.entity.AddressEntity;
import com.glop.users.out.jpadata.entity.ClientEntity;
import com.glop.users.out.jpadata.mapper.ClientOutMapper;
import com.glop.users.out.jpadata.repository.ClientRepository;
import com.glop.users.out.jpadata.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ClientAdapterOut implements ClientPortOut {
    private final ClientRepository clientRepository;
    private final ClientOutMapper clientOutMapper;
    private final UserRepository userRepository;
    private final AddressWriter addressWriter;


    @Override
    public ClientModel create(ClientModel clientModel) {
        ClientEntity client = new ClientEntity();
        client.setUser(userRepository.getReferenceById(clientModel.getUserModel().getUserId()));
        client.setAddress(addressWriter.save(clientModel.getAddressModel()));
        client = clientRepository.save(client);
        return clientOutMapper.toClientModel(client);
    }

    @Override
    public boolean existsByUser(UUID userId) {
        return clientRepository.findByUser_userId(userId).isPresent();
    }
}
