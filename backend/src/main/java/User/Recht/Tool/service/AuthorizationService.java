package User.Recht.Tool.service;

import User.Recht.Tool.dtos.permissionDtos.PermissionRoleDto;
import User.Recht.Tool.entity.Permission;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.*;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.user.UserNotFoundException;

import java.util.List;

public interface AuthorizationService {


    void checkUserManagerAutorisations(User connectedUser, Long targetedUserId, String permissionKey, String token) throws UserNotAuthorized, DeniedRoleLevel, UserNotFoundException;


    void checkRoleToUserAutorisations(User connectedUser, Long targetedUserId, String permissionKey, String token, String roleName, String movedTo)
            throws UserNotAuthorized, DeniedRoleLevel, UserNotFoundException, RoleNotFoundException;

    void checkRoleManagerAutorisations(User connectedUser, String roleName, String permissionKey, String token, String movedTo)
            throws UserNotAuthorized, DeniedRoleLevel, RoleNotFoundException;

    void checkPermissionToRoleAutorisations(User connectedUser, String roleName, String permissionKey, String token,String addPermissionKey)
            throws UserNotAuthorized, DeniedRoleLevel, RoleNotFoundException, PermissionNotValid;

    void checkExistedUserPermission(String permissionKey, String token) throws UserNotAuthorized;

    boolean verifyingAPIAccessAuthorization(User user,String permissionKey,String token) throws PermissionNotFound, EmailNotVerified;

    List<String> getMyPermissions(String token);
    List<PermissionRoleDto>getMyPermissionsObject(User user) throws RoleNotFoundException;
}
