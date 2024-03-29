package User.Recht.Tool.service;

import User.Recht.Tool.dtos.permissionDtos.ListPermissionKeysDto;
import User.Recht.Tool.dtos.permissionDtos.PermissionRoleDto;
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

    @Transactional
    List<String> addPermissionsListToRole(ListPermissionKeysDto listPermissionKeysDto)
            throws RoleNotFoundException, PermissionNotFound, CannotModifySuperAdminException,ArrayIndexOutOfBoundsException, PermissionToRoleNotFound,IllegalArgumentException;

    PermissionRoleDto getPermissionByRole(String permissionKey, String roleName)
            throws PermissionToRoleNotFound, PermissionNotFound, RoleNotFoundException;

    List<PermissionRoleDto> getAll(String roleName) throws RoleNotFoundException;

    List<String> getAllPermissionsOfRole(String roleName)
            throws  RoleNotFoundException;

    PermissionRoleDto updatePermissionRole(PermissionRoleDto permissionRoleDto) throws PermissionNotFound,
            RoleNotFoundException, PermissionToRoleNotFound;

    @Transactional
    PermissionRoleDto deletePermissionRole(String permissionKey, String roleName) throws PermissionNotFound,
            RoleNotFoundException, PermissionToRoleNotFound,CannotModifySuperAdminException;

    @Transactional
    List<String> deleteListePermissionsOfRole(ListPermissionKeysDto listPermissionKeysDto) throws PermissionNotFound,
            RoleNotFoundException, PermissionToRoleNotFound,CannotModifySuperAdminException;

    @Transactional
    List<String> deleteALLPermissionsOfRole(String roleName) throws PermissionNotFound,
            RoleNotFoundException, PermissionToRoleNotFound ,CannotModifySuperAdminException;
}
