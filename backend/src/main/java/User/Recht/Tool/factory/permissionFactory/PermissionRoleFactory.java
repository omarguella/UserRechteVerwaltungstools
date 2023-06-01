package User.Recht.Tool.factory.permissionFactory;


import User.Recht.Tool.dtos.PermissionDtos.PermissionRoleDto;
import User.Recht.Tool.entity.PermissionRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class PermissionRoleFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionRoleFactory.class);

    public PermissionRoleDto permissionRoleFactory(PermissionRole permissionRole) {

        PermissionRoleDto permissionRoleDto = new PermissionRoleDto();

        permissionRoleDto.setId(permissionRole.getId());
        permissionRoleDto.setRoleName(permissionRole.getRole().getName());
        permissionRoleDto.setPermissionKey(permissionRole.getPermission().getKey());
        permissionRoleDto.setType(permissionRole.getType());
        return permissionRoleDto;



    }


}
