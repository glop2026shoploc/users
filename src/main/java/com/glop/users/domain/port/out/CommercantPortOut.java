package com.glop.users.domain.port.out;

import com.glop.users.domain.model.CommercantModel;

public interface CommercantPortOut {
    CommercantModel findByEmail(String email);
    CommercantModel findBySiret(String siret);
    CommercantModel create(CommercantModel commercantModel);
}