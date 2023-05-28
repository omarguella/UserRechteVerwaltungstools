package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.userDtos.UpdateRoleForUsersList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleToUserServiceImpl.class);


    @Transactional
    @Override
    public User addRoleToUser(Long userId, String roleName)
            throws RoleNotFoundException, UserNotFoundException,CannotModifySuperAdminException {

        verifyExistUserAndRole(userId, roleName);

        if (userService.getUserById(userId).getUsername().equals("SUPERADMIN")|| roleName.equalsIgnoreCase("SUPERADMIN")){
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


    @Transactional
    @Override
    public void updateRolesForUsersWithAction(UpdateRoleForUsersList updateRoleForUsersList)
            throws RoleNotFoundException, UserNotFoundException, NullPointerException, CannotModifySuperAdminException, IllegalArgumentException, IllegalAccessException, RoleMovedToException, RoleNotAssignedToUserException {

        if (updateRoleForUsersList.getUsersIdList() == null) {
            throw new IllegalArgumentException("USERSIDLIST SHOULD BE WITH IDS OF THE TYPE LONG");
        }

        Long superAdminId = userService.getUserByUsername("SUPERADMIN").getId();

        if (updateRoleForUsersList.getUsersIdList().contains(superAdminId)) {
            // DELETE SUPERADMIN ID
            List<Long> usersListWithoutSuperAdmin = updateRoleForUsersList.getUsersIdList();
            usersListWithoutSuperAdmin.remove(superAdminId);
            updateRoleForUsersList.setUsersIdList(usersListWithoutSuperAdmin);

        } else if (updateRoleForUsersList.getAction().equalsIgnoreCase("ADD")) {

            if (updateRoleForUsersList.getAddRole() == null) {
                throw new NullPointerException("ADDROLE NOT FOUND");
            }

            for (Long id : updateRoleForUsersList.getUsersIdList()) {
                addRoleToUser(id, updateRoleForUsersList.getAddRole());
            }

        } else if (updateRoleForUsersList.getAction().equalsIgnoreCase("DELETE")) {

            if (updateRoleForUsersList.getDeleteRole() == null || updateRoleForUsersList.getMovedTo() == null) {
                throw new IllegalAccessException("DELETEROLE OR MOVEDTO NOT FOUND");
            }

            usersIdListIsValid(updateRoleForUsersList.getUsersIdList(),updateRoleForUsersList.getDeleteRole());

            for (Long id : updateRoleForUsersList.getUsersIdList()) {
                deleteRoleFromUser(id, updateRoleForUsersList.getDeleteRole(), updateRoleForUsersList.getMovedTo());
            }

        } else {
            throw new IllegalArgumentException("ACTION SHOULD BE ADD OR DELETE");
        }

    }

    @Transactional
    @Override
    public User deleteRoleFromUser(Long userId, String roleName, String userMovedTo)
            throws RoleNotFoundException, UserNotFoundException,RoleNotAssignedToUserException,RoleMovedToException,CannotModifySuperAdminException {

        verifyExistUserAndRole(userId, roleName);

        roleName = roleName.toUpperCase();

        if (userService.getUserById(userId).getUsername().equals("SUPERADMIN")|| userMovedTo.equalsIgnoreCase("SUPERADMIN")){
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
        Role role = roleService.getRoleByName(roleName.toUpperCase());

    }
    public void usersIdListIsValid(List<Long> idList, String roleName) throws RoleNotFoundException, RoleNotAssignedToUserException {

        List<User> listUsers = userService.getAllUsersByRole(roleName);
        boolean allIdsExist = true;

        for (Long id : idList) {
            boolean idExists = false;
            for (User user : listUsers) {
                if (user.getId()==id) {
                    idExists = true;
                    break;
                }
            }
            if (!idExists) {
                throw new RoleNotAssignedToUserException("ROLE NOT AVAILIBALE TO ALL USERS");
            }
        }
    }
}
