package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.roleDtos.RoleDto;
import User.Recht.Tool.dtos.roleDtos.UpdateRoleDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.LevelRoleException;
import User.Recht.Tool.exception.Permission.PermissionNotFound;
import User.Recht.Tool.exception.Permission.PermissionToRoleNotFound;
import User.Recht.Tool.exception.role.RoleMovedToException;
import User.Recht.Tool.exception.role.RoleNameDuplicateElementException;
import User.Recht.Tool.exception.role.RoleNotAssignedToUserException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.factory.roleFactorys.RoleFactory;
import User.Recht.Tool.repository.RoleRepository;
import User.Recht.Tool.service.RoleService;
import User.Recht.Tool.service.RoleToUserService;
import User.Recht.Tool.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequestScoped
public class RoleServiceImpl implements RoleService {

    @Inject
    RoleRepository roleRepository;
    @Inject
    RoleFactory roleFactory;
    @Inject
    RoleToUserService roleToUserService;
    @Inject
    PermissionToRoleServiceImpl permissionToRoleService;
    @Inject
    UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Transactional
    @Override
    public Role createRole(RoleDto roleDto) throws RoleNameDuplicateElementException, RoleNotFoundException, LevelRoleException {
        roleDto.setName(roleDto.getName().toUpperCase());
        if(roleDto.getLevel()<=0){
            throw  new LevelRoleException("LEVEL SHOULD BE BIGGER THAN 0");
        }
        try {
            Role roleCheckWithName = getRoleByName(roleDto.getName());
            throw new RoleNameDuplicateElementException("ROLE NAME " + roleDto.getName() + " EXISTED");
        } catch (RoleNotFoundException ignored) {
        }
        return saveRole(roleDto);

    }

    @Transactional
    @Override
    public Role saveRole(RoleDto roleDto) throws RoleNotFoundException {
        Role role = roleFactory.roleFactory(roleDto);
        roleRepository.persistAndFlush(role);
        return getRoleByName(role.getName());
    }

    @Override
    public Role getRoleByName(String name) throws RoleNotFoundException {
        Role role = roleRepository.find("name", name.toUpperCase()).firstResult();

        if (role != null) {
            return role;
        } else {
            throw new RoleNotFoundException("ROLE DONT EXIST");
        }

    }

    @Override
    public List<Role>  getPublicRoles ()  {
        List<Role>  roles =getAllRoles();

       return roles=roles.stream().filter(role-> !role.getIsPrivate()).collect(Collectors.toList());

    }

    @Override
    public List<Role>  getPrivatRoles ()  {
        List<Role>  roles =getAllRoles();
        return roles=roles.stream().filter(Role::getIsPrivate).collect(Collectors.toList());
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.listAll();
    }

    @Transactional
    @Override
    public Role deleteRoleByName(String roleName, String moveTo) throws RoleNotFoundException, CannotModifySuperAdminException,
            PermissionNotFound, PermissionToRoleNotFound, UserNotFoundException, RoleMovedToException, RoleNotAssignedToUserException,CannotModifySuperAdminException {

        roleName=roleName.toUpperCase();
        moveTo=moveTo.toUpperCase();

        if (roleName.equals("SUPERADMIN")|| moveTo.equals("SUPERADMIN")){
            throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
        }

            Role role = getRoleByName(roleName);

            //Delete Permissions of the Role
            permissionToRoleService.deleteALLPermissionsOfRole(roleName);

            //Delete Role from Users und move to another Role
            List<User> users=userService.getAllUsersByRole(roleName);

            for (User user:users){
                roleToUserService.deleteRoleFromUser(user.getId(),roleName,moveTo);
            }

        roleRepository.delete(role);


            return role;

    }

    @Transactional
    @Override
    public Role updateRoleByName(String name, UpdateRoleDto updateRoleDto)
            throws RoleNotFoundException, RoleNameDuplicateElementException, IllegalArgumentException,CannotModifySuperAdminException {

      /*  if (name.equalsIgnoreCase("SUPERADMIN")){
            throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
        }*/

        Role roleToUpdate = getRoleByName(name);

        if (updateRoleDto.getName() != null) {
            try {
                updateRoleDto.setName(updateRoleDto.getName().toUpperCase());
                if (updateRoleDto.getName().equals("SUPERADMIN")) {
                    throw new IllegalArgumentException("CANNOT CHANGE SUPERADMIN NAME");
                }
                Role checkName = getRoleByName(updateRoleDto.getName());
                if (!Objects.equals(checkName.getId(), roleToUpdate.getId())) {
                    throw new RoleNameDuplicateElementException("USERNAME ALREADY USED");
                }
            } catch (RoleNotFoundException ignored) {
            }
        }
        roleToUpdate = roleFactory.updateRoleFactory(roleToUpdate, updateRoleDto);
        return saveUpdatedRole(roleToUpdate);
    }

    @Transactional
    public Role saveUpdatedRole(Role role) {
        roleRepository.getEntityManager().merge(role);
        return role;
    }



}
