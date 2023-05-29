package User.Recht.Tool.service;

import User.Recht.Tool.dtos.PermissionDtos.PermissionDto;
import User.Recht.Tool.entity.Permission;
import User.Recht.Tool.exception.Permission.*;

import javax.transaction.Transactional;
import java.util.List;

public interface PermissionService {
    @Transactional
    List<Permission> createPermission(PermissionDto permissionDto) throws IllegalArgumentException, IllegalAccessException, PermissionNotFound;

    Permission getPermissionByKey(String key) throws PermissionNotFound;

    List<Permission> getPermissionsByName(String name);

    List<Permission> getAllPermissions();

    @Transactional
    Permission deletePermission(String key) throws  PermissionNotFound;

    boolean isValidName(String name);
}
