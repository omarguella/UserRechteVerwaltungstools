package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.DeniedRoleLevel;
import User.Recht.Tool.exception.Permission.UserNotAuthorized;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.AutorisationService;
import User.Recht.Tool.service.ClaimsOfUser;
import User.Recht.Tool.service.RoleService;
import User.Recht.Tool.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.core.io.NumberInput.parseInt;

@RequestScoped
public class AutorisationServiceImpl implements AutorisationService {

    @Inject
    ClaimsOfUser claimsOfUser;
    @Inject
    RoleService roleService;

    @Inject
    UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimsOfUserImpl.class);

    @Override
    public void checkUserManagerAutorisations(User connectedUser, Long targetedUserId, String permissionKey, String token)
            throws UserNotAuthorized, DeniedRoleLevel, UserNotFoundException {


        String[] parts = permissionKey.split("_");
        String apiFunc = parts[parts.length - 1];

        if (targetedUserId != null && (targetedUserId != connectedUser.getId())) {

            checkExistedUserPermission(permissionKey, token);
            User targetedUser = userService.getUserById(targetedUserId);
            if (apiFunc.equals("DELETE")) {
                if (getMinimumRoleLevelFromToken(token) >= getMinimumRoleLevelOfTargetUser(targetedUser)) {
                    throw new DeniedRoleLevel("CANNOT " + apiFunc + " A USER OF A HIGHER OR SAME ROLE LEVEL");
                }
            } else {
                if (getMinimumRoleLevelFromToken(token) > getMinimumRoleLevelOfTargetUser(targetedUser)) {
                    throw new DeniedRoleLevel("CANNOT " + apiFunc + " A USER OF A HIGHER  ROLE LEVEL");
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

        //Cannot add/Delete update User with a Role Level Higher of my Self
        if (role.getLevel() < getMinimumRoleLevelFromToken(token)) {
            throw new DeniedRoleLevel("CANNOT " + apiFunc + " A ROLE TO/FROM USER OF A HIGHER OR SAME ROLE LEVEL");
        }

        if (movedTo != null) {
            if (!movedTo.isEmpty()) {
                Role movedToRole = roleService.getRoleByName(movedTo);
                if (movedToRole.getLevel() < getMinimumRoleLevelFromToken(token)) {
                    throw new DeniedRoleLevel("CANNOT " + apiFunc + " A ROLE TO/FROM USER OF A HIGHER OR SAME ROLE LEVEL");
                }
            }
        }

        //Only for sub Users
        User targetedUser = userService.getUserById(targetedUserId);
        if (getMinimumRoleLevelFromToken(token) >= getMinimumRoleLevelOfTargetUser(targetedUser))
            throw new DeniedRoleLevel("CANNOT " + apiFunc + " A ROLE TO/FROM USER OF A HIGHER OR SAME ROLE LEVEL");

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
                    throw new DeniedRoleLevel("CANNOT " + apiFunc + " A ROLE TO/FROM USER OF A HIGHER OR SAME ROLE LEVEL");
                }
            }
        }

            if (apiFunc.equals("DELETE")) {
            if (getMinimumRoleLevelFromToken(token) >= role.getLevel()) {
                throw new DeniedRoleLevel("CANNOT " + apiFunc + " A USER OF A HIGHER OR SAME ROLE LEVEL");
            }
        } else {
            if (getMinimumRoleLevelFromToken(token) > role.getLevel()) {
                throw new DeniedRoleLevel("CANNOT " + apiFunc + " A USER OF A HIGHER  ROLE LEVEL");
            }
        }

    }


    @Override
    public void checkPermissionToRoleAutorisations(User connectedUser, String roleName, String permissionKey, String token, String addPermissionKey)
            throws UserNotAuthorized, DeniedRoleLevel, RoleNotFoundException {

        String[] parts = permissionKey.split("_");
        String apiFunc = parts[parts.length - 1];

        Role role = roleService.getRoleByName(roleName);

        checkExistedUserPermission(permissionKey, token);

        if (addPermissionKey!=null){
            checkExistedUserPermission(addPermissionKey, token);
        }


        if (getMinimumRoleLevelFromToken(token) >= role.getLevel()) {
            throw new DeniedRoleLevel("CANNOT " + apiFunc + " A USER OF A HIGHER OR SAME ROLE LEVEL");
        }
    }



    @Override
    public void checkExistedUserPermission(String permissionKey, String token) throws UserNotAuthorized {
        permissionKey=permissionKey.toUpperCase();
        if (!getMyPermissions(token).contains(permissionKey + "_ALL")) {
            throw new UserNotAuthorized("USER IS NOT AUTHOROZIED FOR THE PERMISSION");
        }
    }


    @Override
    public List<String> getMyPermissions(String token) {

        Map<String, Object> map = claimsOfUser.listClaimUsingJWT(token);
        List<String> list = (List<String>) map.get("allPermissionsOfUser");
        return list;

    }

    private int getMinimumRoleLevelFromToken(String token) {
        Map<String, Object> allClaims = claimsOfUser.listClaimUsingJWT(token);
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
