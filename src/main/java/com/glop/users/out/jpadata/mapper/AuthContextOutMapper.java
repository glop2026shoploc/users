package com.glop.users.out.jpadata.mapper;

import com.glop.users.domain.model.AuthContext;
import com.glop.users.domain.model.EmployeeRole;
import com.glop.users.domain.model.UserRole;
import com.glop.users.out.jpadata.entity.AdminEntity;
import com.glop.users.out.jpadata.entity.ClientEntity;
import com.glop.users.out.jpadata.entity.EmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthContextOutMapper {


    @Mapping(target = "userRole", expression = "java(getClientRole())")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "shopId", ignore = true)
    @Mapping(target = "employeeRole", ignore = true)
    @Mapping(target = "employeeId", source = "clientId")
    @Mapping(target = "cityId", source = "city.cityId")
    @Mapping(target = "adminId", ignore = true)
    @Mapping(target = "clientId",source = "clientId" )
    AuthContext toAuthContext(ClientEntity clientEntity);

    @Mapping(target = "userRole", expression = "java(getEmployeeRole())")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "shopId", source = "shop.shopId")
    @Mapping(target = "employeeRole", source = "role")
    @Mapping(target = "employeeId", source = "employeeId")
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "cityId", source = "shop.address.city.cityId")
    @Mapping(target = "adminId", ignore = true)
    AuthContext toAuthContext(EmployeeEntity employeeEntity);

    default EmployeeRole toEmployeeRole(com.glop.users.out.jpadata.entity.EmployeeRole employeeRole){
        return switch (employeeRole){
            case OWNER -> EmployeeRole.OWNER;
            case EMPLOYEE -> EmployeeRole.EMPLOYEE;
            case null, default -> null;
        };
    }
    @Mapping(target = "userRole", expression = "java(getAdminRole())")
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "employeeRole", ignore = true)
    @Mapping(target = "employeeId", ignore = true)
    @Mapping(target = "clientId", ignore = true)
    @Mapping(target = "cityId", source = "city.cityId")
    @Mapping(target = "shopId", ignore = true)
    @Mapping(target = "adminId", source = "adminId")
    AuthContext toAuthContext(AdminEntity adminEntity);

    default UserRole getAdminRole(){return UserRole.ADMIN;}
    default UserRole getEmployeeRole(){return UserRole.MERCHANT;}
    default UserRole getClientRole(){return UserRole.CLIENT;}
}
