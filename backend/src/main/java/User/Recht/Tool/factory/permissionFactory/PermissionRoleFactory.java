package User.Recht.Tool.factory.permissionFactory;


import User.Recht.Tool.dtos.permissionDtos.PermissionRoleDto;
import User.Recht.Tool.entity.PermissionRole;
import User.Recht.Tool.exception.Permission.PermissionNotFound;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.service.PermissionService;
import User.Recht.Tool.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

@RequestScoped
public class PermissionRoleFactory {

    @Inject
    RoleService roleService;
    @Inject
    PermissionService permissionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionRoleFactory.class);

    public PermissionRoleDto permissionRoleDtoFactory(PermissionRole permissionRole) {

        PermissionRoleDto permissionRoleDto = new PermissionRoleDto();

        permissionRoleDto.setId(permissionRole.getId());
        permissionRoleDto.setRoleName(permissionRole.getRole().getName());
        permissionRoleDto.setPermissionKey(permissionRole.getPermission().getKey());
        permissionRoleDto.setType(permissionRole.getType());
        return permissionRoleDto;

    }

    public PermissionRole permissionRoleFactory(PermissionRoleDto permissionRoleDto) throws RoleNotFoundException, PermissionNotFound {

        PermissionRole permissionRole = new PermissionRole();

        permissionRole.setId(permissionRoleDto.getId());
        permissionRole.setRole(roleService.getRoleByName(permissionRoleDto.getRoleName()));
        permissionRole.setPermission(permissionService.getPermissionByKey(permissionRoleDto.getPermissionKey()));
        permissionRole.setType(permissionRoleDto.getType());

        return permissionRole;

    }


}
