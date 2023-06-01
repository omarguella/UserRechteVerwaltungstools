package User.Recht.Tool.service;

import User.Recht.Tool.dtos.PermissionDtos.PermissionDto;
import User.Recht.Tool.entity.Permission;
import User.Recht.Tool.exception.Permission.*;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;

import javax.transaction.Transactional;
import java.util.List;

public interface PermissionService {
    @Transactional
    List<Permission> createPermission(PermissionDto permissionDto) throws IllegalArgumentException, IllegalAccessException, PermissionNotFound;

    Permission getPermissionByKey(String key) throws PermissionNotFound;


    List<Permission> getPermissionsByName(String name) ;

    List<Permission> getAllPermissions();

    @Transactional
    Permission deletePermissionByKey(String key) throws  PermissionNotFound, CannotModifySuperAdminException, RoleNotFoundException,PermissionToRoleNotFound;

    @Transactional
    List<Permission> deletePermissionsByName(String name) throws PermissionNotFound, CannotModifySuperAdminException, RoleNotFoundException, PermissionToRoleNotFound;

    @Transactional
    Permission saveUpdatedPermission(Permission permission);

    boolean isValidName(String name);
}
