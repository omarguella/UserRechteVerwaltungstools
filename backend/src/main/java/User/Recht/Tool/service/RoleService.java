package User.Recht.Tool.service;

import User.Recht.Tool.dtos.roleDtos.RoleDto;
import User.Recht.Tool.dtos.roleDtos.UpdateRoleDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.exception.role.RoleNameDuplicateElementException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;

import javax.transaction.Transactional;
import java.util.List;

public interface RoleService {
    @Transactional
    Role createRole(RoleDto roleDto) throws RoleNameDuplicateElementException, RoleNotFoundException;

    @Transactional
    Role saveRole(RoleDto roleDto) throws RoleNotFoundException;

    Role getRoleByName(String name) throws RoleNotFoundException;

    List<Role> getAllRoles();

    @Transactional
    Role deleteRoleByName(String name) throws RoleNotFoundException, CannotModifySuperAdminException;

    @Transactional
    Role updateRoleByName (String name, UpdateRoleDto updateRoleDto)
            throws RoleNotFoundException, RoleNameDuplicateElementException,IllegalArgumentException,CannotModifySuperAdminException;

}
