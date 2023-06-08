package User.Recht.Tool.service;

import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.DeniedRoleLevel;
import User.Recht.Tool.exception.Permission.UserNotAuthorized;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.user.UserNotFoundException;

import java.util.List;

public interface AutorisationService {


    void checkUserManagerAutorisations(User connectedUser, Long targetedUserId, String permissionKey, String token) throws UserNotAuthorized, DeniedRoleLevel, UserNotFoundException;


    void checkRoleToUserAutorisations(User connectedUser, Long targetedUserId, String permissionKey, String token, String roleName, String movedTo)
            throws UserNotAuthorized, DeniedRoleLevel, UserNotFoundException, RoleNotFoundException;

    void checkRoleManagerAutorisations(User connectedUser, String roleName, String permissionKey, String token, String movedTo)
            throws UserNotAuthorized, DeniedRoleLevel, RoleNotFoundException;

    void checkExistedUserPermission(String permissionKey, String token) throws UserNotAuthorized;

    List<String> getMyPermissions(String token);
}
