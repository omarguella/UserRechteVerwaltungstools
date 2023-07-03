package User.Recht.Tool.service;

import User.Recht.Tool.dtos.roleDtos.RoleDto;
import User.Recht.Tool.dtos.roleDtos.UpdateRoleDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.LevelRoleException;
import User.Recht.Tool.exception.Permission.PermissionNotFound;
import User.Recht.Tool.exception.Permission.PermissionToRoleNotFound;
import User.Recht.Tool.exception.role.*;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;

import javax.transaction.Transactional;
import java.util.List;

public interface RoleService {
    @Transactional
    Role createRole(RoleDto roleDto,String token) throws RoleNameDuplicateElementException, RoleNotFoundException, LevelRoleException;

    @Transactional
    Role saveRole(RoleDto roleDto) throws RoleNotFoundException;

    Role getRoleByName(String name) throws RoleNotFoundException;

    List<Role>  getPublicRoles();

    List<Role>  getPrivatRoles(User user);

    List<Role> getAllRoles();


    List<Role> getAvailibaleRoles(User user);
    List<Role> getAvailibaleRolesToEdit(User user,String token);

    @Transactional
    Role deleteRoleByName(User user, String token,String roleName, String moveTo) throws RoleNotFoundException, CannotModifySuperAdminException,
            PermissionNotFound, PermissionToRoleNotFound, UserNotFoundException, RoleMovedToException, RoleNotAssignedToUserException, RoleNotAccessibleException;

    @Transactional
    Role updateRoleByName (String name, UpdateRoleDto updateRoleDto, String token)
            throws RoleNotFoundException, RoleNameDuplicateElementException, IllegalArgumentException, LevelRoleException;

}
