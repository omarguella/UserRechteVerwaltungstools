package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.permissionDtos.PermissionRoleDto;
import User.Recht.Tool.entity.Permission;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.*;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.fasterxml.jackson.core.io.NumberInput.parseInt;

@RequestScoped
public class AuthorizationServiceImpl implements AuthorizationService {

    @Inject
    ClaimsOfUserService claimsOfUserService;
    @Inject
    RoleService roleService;
    @Inject
    PermissionToRoleService permissionToRoleService;
    @Inject
    UserService userService;

    @Inject
    PermissionService permissionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimsOfUserServiceImpl.class);

    @Override
    public void checkUserManagerAutorisations(User connectedUser, Long targetedUserId, String permissionKey, String token) throws UserNotAuthorized, DeniedRoleLevel, UserNotFoundException {


        String[] parts = permissionKey.split("_");
        String apiFunc = parts[parts.length - 1];

        if (targetedUserId != null && (targetedUserId != connectedUser.getId())) {

            checkExistedUserPermission(permissionKey, token);

            User targetedUser = userService.getUserById(targetedUserId);

            if (apiFunc.equals("DELETE")|| apiFunc.equals("PUT")) {
                if (getMinimumRoleLevelFromToken(token) >= getMinimumRoleLevelOfTargetUser(targetedUser)) {
                    throw new DeniedRoleLevel("CANNOT " + apiFunc + " A USER OF A LOWER OR SAME ROLE LEVEL");
                }
            } else {
                if (getMinimumRoleLevelFromToken(token) > getMinimumRoleLevelOfTargetUser(targetedUser)) {
                    throw new DeniedRoleLevel("CANNOT " + apiFunc + " A USER OF A LOWER  ROLE LEVEL");
                }
            }
        }
    }

    @Override
    public void checkRoleToUserAutorisations(User connectedUser, Long targetedUserId, String permissionKey, String token, String roleName, String movedTo)
            throws UserNotAuthorized, DeniedRoleLevel, UserNotFoundException, RoleNotFoundException {


        String[] parts = permissionKey.split("_");
        String apiFunc = parts[parts.length - 1];


        //Cannot add/Delete a Role to my Self
        if (targetedUserId != null && (targetedUserId == connectedUser.getId())) {
            throw new UserNotAuthorized("USER IS NOT AUTHOROZIED FOR THE PERMISSION");
        }
        checkExistedUserPermission(permissionKey, token);
        Role role = roleService.getRoleByName(roleName);

        //Cannot add/Delete update User with a Role Level LOWER of my Self
        if (role.getLevel() < getMinimumRoleLevelFromToken(token)) {
            throw new DeniedRoleLevel("CANNOT " + apiFunc + " A ROLE TO/FROM USER OF A LOWER OR SAME ROLE LEVEL");
        }

        if (movedTo != null) {
            if (!movedTo.isEmpty()) {
                Role movedToRole = roleService.getRoleByName(movedTo);
                if (movedToRole.getLevel() < getMinimumRoleLevelFromToken(token)) {
                    throw new DeniedRoleLevel("CANNOT " + apiFunc + " A ROLE TO/FROM USER OF A LOWER OR SAME ROLE LEVEL");
                }
            }
        }

        //Only for sub Users
        User targetedUser = userService.getUserById(targetedUserId);
        if (getMinimumRoleLevelFromToken(token) >= getMinimumRoleLevelOfTargetUser(targetedUser))
            throw new DeniedRoleLevel("CANNOT " + apiFunc + " A ROLE TO/FROM USER OF A LOWER OR SAME ROLE LEVEL");

    }


    @Override
    public void checkRoleManagerAutorisations(User connectedUser, String roleName, String permissionKey, String token, String movedTo)
            throws UserNotAuthorized, DeniedRoleLevel, RoleNotFoundException {
        String[] parts = permissionKey.split("_");
        String apiFunc = parts[parts.length - 1];

        Role role = roleService.getRoleByName(roleName);
        checkExistedUserPermission(permissionKey, token);

        if (movedTo != null) {
            if (!movedTo.isEmpty()) {
                Role movedToRole = roleService.getRoleByName(movedTo);
                if (movedToRole.getLevel() < getMinimumRoleLevelFromToken(token)) {
                    throw new DeniedRoleLevel("CANNOT " + apiFunc + " A ROLE TO/FROM USER OF A LOWER OR SAME ROLE LEVEL");
                }
            }
        }

            if (apiFunc.equals("DELETE")) {
            if (getMinimumRoleLevelFromToken(token) >= role.getLevel()) {
                throw new DeniedRoleLevel("CANNOT " + apiFunc + " A USER OF A LOWER OR SAME ROLE LEVEL");
            }
        } else {
            if (getMinimumRoleLevelFromToken(token) > role.getLevel()) {
                throw new DeniedRoleLevel("CANNOT " + apiFunc + " A USER OF A LOWER  ROLE LEVEL");
            }
        }

    }


    @Override
    public void checkPermissionToRoleAutorisations(User connectedUser, String roleName, String permissionKey, String token, String addPermissionKey)
            throws UserNotAuthorized, DeniedRoleLevel, RoleNotFoundException, PermissionNotValid {

        if (!connectedUser.getUsername().equals("SUPERADMIN")) {
            String[] parts = permissionKey.split("_");
            String apiFunc = parts[parts.length - 1];

            Role role = roleService.getRoleByName(roleName);

            checkExistedUserPermission(permissionKey, token);

            if (addPermissionKey != null) {
                checkAddPermissionKey(addPermissionKey, token);
            }


            if (getMinimumRoleLevelFromToken(token) >= role.getLevel()) {
                throw new DeniedRoleLevel("CANNOT " + apiFunc + " A USER OF A LOWER OR SAME ROLE LEVEL");
            }
        }
    }


    @Override
    public void checkExistedUserPermission(String permissionKey, String token) throws UserNotAuthorized {
        permissionKey = permissionKey.toUpperCase();
        if (!getMyPermissions(token).contains(permissionKey + "_ALL")) {
            throw new UserNotAuthorized("USER IS NOT AUTHOROZIED FOR THE PERMISSION");
        }
    }
    public void checkAddPermissionKey(String addPermissionKeyWithType, String token) throws UserNotAuthorized, PermissionNotValid {
        addPermissionKeyWithType = addPermissionKeyWithType.toUpperCase();
        String[] parts = addPermissionKeyWithType.split("_");
        String type = parts[parts.length - 1];
        String PermissionKey=addPermissionKeyWithType.substring(0,addPermissionKeyWithType.indexOf(type)-1);

        LOGGER.info(PermissionKey);

        if (!getMyPermissions(token).contains(addPermissionKeyWithType)) {
            if(!getMyPermissions(token).contains(PermissionKey+"_ALL")){
                throw new PermissionNotValid("CANNOT ADD A PERMISSION OF LOWER TYPE");

            }
        }
    }


    @Override
    public boolean verifyingAPIAccessAuthorization(User user, String permissionKey, String token) throws PermissionNotFound, IllegalArgumentException, EmailNotVerified {

        permissionKey = permissionKey.toUpperCase();
        String[] parts = permissionKey.split("_");
        String type = parts[parts.length - 1];

        if (!(type.equals("ALL") || type.equals("ME"))) {
            throw new IllegalArgumentException("TYPE SHOULD BE ALL OR ME");
        }

        Map<String, Object> map = claimsOfUserService.listClaimUsingJWT(token);
        boolean isMailToVerify = (boolean) map.get("isMailToVerify");

        if (isMailToVerify && !user.getIsVerifiedEmail()) {
            throw new EmailNotVerified("THE EMAIL IS NOT VERIFIED");
        }


        permissionKey = permissionKey.substring(0, permissionKey.indexOf(type) - 1);


        Permission checkExistedPermission = permissionService.getPermissionByKey(permissionKey);


        List<PermissionRoleDto> allPermissionsOfUser = user.getRoles().stream().flatMap(role -> {
            try {
                List<PermissionRoleDto> permissionsOfRole = permissionToRoleService.getAll(role.getName());
                return permissionsOfRole.stream();
            } catch (RoleNotFoundException ignored) {
                return Stream.empty();
            }
        }).distinct().toList();

        for (PermissionRoleDto permissionRoleDto : allPermissionsOfUser) {
            if (permissionRoleDto.getPermissionKey().equals(permissionKey)) {
                if (permissionRoleDto.getType().equals("ALL")) {
                    return true;
                } else if (permissionRoleDto.getType().equals(type)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> getMyPermissions(String token) {

        Map<String, Object> map = claimsOfUserService.listClaimUsingJWT(token);
        List<String> list = (List<String>) map.get("allPermissionsOfUser");
        return list;

    }
    @Override
    public List<PermissionRoleDto> getMyPermissionsObject(User user) throws RoleNotFoundException {

        List<Role> roles=user.getRoles();
        List<PermissionRoleDto> permissions=new ArrayList<>();
        for(Role role:roles){
            permissions.addAll(permissionToRoleService.getAll(role.getName()));
        }

        return permissions.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private int getMinimumRoleLevelFromToken(String token) {
        Map<String, Object> allClaims = claimsOfUserService.listClaimUsingJWT(token);
        int minRoleLevel = parseInt(String.valueOf((Long) allClaims.get("minRoleLevel")));
        return minRoleLevel;
    }

    private int getMinimumRoleLevelOfTargetUser(User user) {
        int minRoleLevel = user.getRoles()
                .stream()
                .mapToInt(Role::getLevel)
                .min()
                .orElse(1);
        return minRoleLevel;
    }

}
