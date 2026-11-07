package com.glop.users.domain.model;

import lombok.Data;

import java.util.UUID;
@Data

public class AuthContext {
    private String email;
    private UserRole userRole;
    private UUID userId;
    private UUID shopId;
    private UUID clientId;
    private UUID adminId;
    private UUID employeeId;

    private EmployeeRole employeeRole;

    private UUID cityId;

}
