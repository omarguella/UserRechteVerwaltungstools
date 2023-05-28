package User.Recht.Tool.service;

import User.Recht.Tool.dtos.userDtos.UpdateRoleForUsersList;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.role.RoleMovedToException;
import User.Recht.Tool.exception.role.RoleNotAssignedToUserException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;

import javax.transaction.Transactional;

public interface RoleToUserService {
    @Transactional
    User addRoleToUser(Long userId, String roleName)
            throws RoleNotFoundException, UserNotFoundException,CannotModifySuperAdminException;

    @Transactional
    void updateRolesForUsersWithAction(UpdateRoleForUsersList updateRoleForUsersList)
            throws RoleNotFoundException, UserNotFoundException, NullPointerException,CannotModifySuperAdminException,IllegalArgumentException, IllegalAccessException, RoleMovedToException, RoleNotAssignedToUserException;

    @Transactional
    User deleteRoleFromUser(Long userId, String roleName, String userMovedTo)
            throws RoleNotFoundException, UserNotFoundException, RoleNotAssignedToUserException, CannotModifySuperAdminException, RoleMovedToException;
}
