package com.glop.users.domain.port.in;

import com.glop.users.domain.model.CommercantModel;
import org.apache.coyote.BadRequestException;

public interface CommercantPortIn {
    CommercantModel register(CommercantModel commercantModel) throws BadRequestException;
}
