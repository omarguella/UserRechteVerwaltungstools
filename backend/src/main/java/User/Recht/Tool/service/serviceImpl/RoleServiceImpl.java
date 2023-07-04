package User.Recht.Tool.service.serviceImpl;

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
import User.Recht.Tool.factory.roleFactorys.RoleFactory;
import User.Recht.Tool.repository.RoleRepository;
import User.Recht.Tool.service.ClaimsOfUserService;
import User.Recht.Tool.service.RoleService;
import User.Recht.Tool.service.RoleToUserService;
import User.Recht.Tool.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
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
    @Inject
    ClaimsOfUserService claimsOfUserService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Transactional
    @Override
    public Role createRole(RoleDto roleDto, String token) throws RoleNameDuplicateElementException, RoleNotFoundException, LevelRoleException {
        roleDto.setName(roleDto.getName().toUpperCase());

        Map<String, Object> map = claimsOfUserService.listClaimUsingJWT(token);
        Long minRoleLevel = (Long) map.get("minRoleLevel");


        if (roleDto.getLevel() <= minRoleLevel) {
            throw new LevelRoleException("LEVEL SHOULD BE BIGGER THAN THE MINIMUM LEVEL OF THE ROLE");
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
    public Role getRoleById(Long id) throws RoleNotFoundException {
        Role role = roleRepository.findById(id);

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
    public List<Role>  getPrivatRoles (User user)  {
        List<Role>  roles =getAllRoles();
        int minRoleLevel = user.getRoles()
                .stream()
                .mapToInt(Role::getLevel)
                .min()
                .orElse(1);

        return roles.stream()
                .filter(r -> r.getIsPrivate() && r.getLevel() >= minRoleLevel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Role> getAllRoles() {

        return roleRepository.listAll();
    }

    @Override
    public List<Role> getAvailibaleRoles(User user) {


        int minRoleLevel = user.getRoles()
                .stream()
                .mapToInt(Role::getLevel)
                .min()
                .orElse(1);

        List<Role> allRoles=getAllRoles();

        return allRoles.stream().filter(r-> r.getLevel()>=minRoleLevel).collect(Collectors.toList());
    }

    @Override
    public List<Role> getAvailibaleRolesToEdit(User user,String token) {


        int minRoleLevel = user.getRoles()
                .stream()
                .mapToInt(Role::getLevel)
                .min()
                .orElse(1);

        List<Role> allRoles=getAllRoles();

        return allRoles.stream().filter(r-> r.getLevel()>minRoleLevel).collect(Collectors.toList());
    }




    @Transactional
    @Override
    public Role deleteRoleByName(User connectedUser,String token, String roleName, String moveTo) throws RoleNotFoundException,
            PermissionNotFound, PermissionToRoleNotFound, UserNotFoundException,
            RoleMovedToException, RoleNotAssignedToUserException, CannotModifySuperAdminException,
            RoleNotAccessibleException {

        roleName=roleName.toUpperCase();
        moveTo=moveTo.toUpperCase();

        if (roleName.equals("SUPERADMIN")|| moveTo.equals("SUPERADMIN")){
            throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
        }

            Role role = getRoleByName(roleName);

            //Delete Permissions of the Role
            permissionToRoleService.deleteALLPermissionsOfRole(roleName);

            //Delete Role from Users und move to another Role
            List<User> users=userService.getAllUsersByRole(connectedUser,token,roleName);

            for (User user:users){
                roleToUserService.deleteRoleFromUser(user.getId(),roleName,moveTo);
            }

        roleRepository.delete(role);


            return role;

    }

    @Transactional
    @Override
    public Role updateRoleByName(String name, UpdateRoleDto updateRoleDto,String token)
            throws RoleNotFoundException, RoleNameDuplicateElementException, IllegalArgumentException, LevelRoleException {


        Role roleToUpdate = getRoleByName(name);


        if (updateRoleDto.getName() != null) {
            updateRoleDto.setName(updateRoleDto.getName().toUpperCase());

            try {
                Role checkName = getRoleByName(updateRoleDto.getName());
                if (!Objects.equals(checkName.getId(), roleToUpdate.getId())) {
                    throw new RoleNameDuplicateElementException("ROLE NAME ALREADY USED");
                }
            } catch (RoleNotFoundException ignored) {
            }
        }

        if (updateRoleDto.getLevel() != 0) {

            Map<String, Object> map = claimsOfUserService.listClaimUsingJWT(token);
            Long minRoleLevel = (Long) map.get("minRoleLevel");

            if(minRoleLevel>=updateRoleDto.getLevel() ){
                throw new LevelRoleException("CANNOT UPDATE A LEVEL HIGHER THEN THE CURRENT LEVEL OF USER ");
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
