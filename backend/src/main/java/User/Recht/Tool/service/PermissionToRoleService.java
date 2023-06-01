package User.Recht.Tool.service;

import User.Recht.Tool.dtos.PermissionDtos.PermissionRoleDto;
import User.Recht.Tool.entity.Permission;
import User.Recht.Tool.entity.PermissionRole;
import User.Recht.Tool.exception.Permission.PermissionNotFound;
import User.Recht.Tool.exception.Permission.PermissionToRoleNotFound;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;

import javax.transaction.Transactional;
import java.util.List;

public interface PermissionToRoleService {


    @Transactional
    PermissionRoleDto addPermissionToRole(PermissionRoleDto permissionRoleDto)
            throws RoleNotFoundException, PermissionNotFound, CannotModifySuperAdminException, PermissionToRoleNotFound,IllegalArgumentException;

    PermissionRoleDto getPermissionByRole(String permissionKey, String roleName)
            throws PermissionToRoleNotFound, PermissionNotFound, RoleNotFoundException;

    List<String> getAllPermissionsOfRole(String roleName)
            throws  RoleNotFoundException;

    PermissionRoleDto updatePermissionRole(PermissionRoleDto permissionRoleDto) throws PermissionNotFound,
            RoleNotFoundException, PermissionToRoleNotFound;
}
