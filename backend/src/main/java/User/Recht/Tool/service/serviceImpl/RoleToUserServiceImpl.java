package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.role.RoleMovedToException;
import User.Recht.Tool.exception.role.RoleNotAssignedToUserException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.RoleService;
import User.Recht.Tool.service.RoleToUserService;
import User.Recht.Tool.service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
public class RoleToUserServiceImpl implements RoleToUserService {

    @Inject
    UserService userService;

    @Inject
    RoleService roleService;

    @Transactional
    @Override
    public User addRoleToUser(Long userId, String roleName)
            throws RoleNotFoundException, UserNotFoundException,CannotModifySuperAdminException {

        verifyExistUserAndRole(userId, roleName);

        if (userService.getUserById(userId).getUsername().equals("SUPERADMIN")){
            throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
        }

        User user = userService.getUserById(userId);
        Role role = roleService.getRoleByName(roleName);
        List<Role> roles = user.getRoles();
        if (!roles.contains(role)) {
            roles.add(role);
        }
        user.setRoles(roles);
        userService.saveUpdatedUser(user);
        return userService.getUserById(userId);
    }

    /*
    @Transactional
    @Override
    public User updateRolesToUser(Long userId, String roleName)
            throws RoleNotFoundException, UserNotFoundException {
          // Update liste Users to Role
    }*/

    @Transactional
    @Override
    public User deleteRoleFromUser(Long userId, String roleName, String userMovedTo)
            throws RoleNotFoundException, UserNotFoundException,RoleNotAssignedToUserException,RoleMovedToException,CannotModifySuperAdminException {

        verifyExistUserAndRole(userId, roleName);


        if (userService.getUserById(userId).getUsername().equals("SUPERADMIN")){
                throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
            }


        User user = userService.getUserById(userId);
        Role role = roleService.getRoleByName(roleName);
        List<Role> roles = user.getRoles();

        if (!roles.contains(role)) {
            throw new RoleNotAssignedToUserException("ROLE NOT AVAILIBALE TO THE USER");
        } else{
            roles.remove(role);
            if(roles.size()==0){
                if(userMovedTo==null){
                    throw new RoleMovedToException("ROLE TO MOVE NOT FOUND");
                } else {

                        Role movedTo= roleService.getRoleByName(userMovedTo);
                        roles.add(roleService.getRoleByName(userMovedTo));
                }
            }
        }
        user.setRoles(roles);
        userService.saveUpdatedUser(user);
        return userService.getUserById(userId);
    }

    public void verifyExistUserAndRole(Long userId, String roleName)
            throws RoleNotFoundException, UserNotFoundException {

        User user = userService.getUserById(userId);
        Role role = roleService.getRoleByName(roleName);

    }
}
