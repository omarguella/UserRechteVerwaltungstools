package User.Recht.Tool.service.serviceImpl;


import User.Recht.Tool.dtos.permissionDtos.ListPermissionKeysDto;
import User.Recht.Tool.dtos.permissionDtos.PermissionRoleDto;
import User.Recht.Tool.entity.Permission;
import User.Recht.Tool.entity.PermissionRole;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.exception.Permission.PermissionNotFound;
import User.Recht.Tool.exception.Permission.PermissionToRoleNotFound;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.factory.permissionFactory.PermissionRoleFactory;
import User.Recht.Tool.service.PermissionService;
import User.Recht.Tool.service.PermissionToRoleService;
import User.Recht.Tool.repository.PermissionRoleRepository;
import User.Recht.Tool.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class PermissionToRoleServiceImpl implements PermissionToRoleService {

    @Inject
    PermissionService permissionService;
    @Inject
    RoleService roleService;
    @Inject
    PermissionRoleRepository permissionRoleRepository;
    @Inject
    PermissionRoleFactory permissionRoleFactory;


    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionToRoleServiceImpl.class);
    @Inject
    EntityManager entityManager;


    @Transactional
    @Override
    public PermissionRoleDto addPermissionToRole(PermissionRoleDto permissionRoleDto)
            throws RoleNotFoundException, PermissionNotFound, CannotModifySuperAdminException, PermissionToRoleNotFound,IllegalArgumentException {

        String roleName=permissionRoleDto.getRoleName().toUpperCase();
        String permissionKey=permissionRoleDto.getPermissionKey().toUpperCase();
        String type=permissionRoleDto.getType().toUpperCase();

        verifyExistPermissionAndRole(permissionKey,roleName);

      /*  if (roleService.getRoleByName(roleName).getName().equalsIgnoreCase("SUPERADMIN")){
            throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
        }*/

        if(!(type.equals("ALL") || type.equals("ONE"))){
            throw new IllegalArgumentException("TYPE SHOULD BE ALL OR ONE");
        }

        try {
          PermissionRoleDto existedPermission= getPermissionByRole(permissionKey,roleName);
          return existedPermission;
        }catch (PermissionToRoleNotFound ignored){}

        Permission permission = permissionService.getPermissionByKey(permissionKey);
        Role role = roleService.getRoleByName(roleName);


        PermissionRole permissionRole = new PermissionRole();
        permissionRole.setRole(role);
        permissionRole.setPermission(permission);
        permissionRole.setType(type);


        entityManager.persist(permissionRole);
        return getPermissionByRole(permissionKey,roleName);

            }

    @Transactional
    @Override
    public List<String> addPermissionsListToRole(ListPermissionKeysDto listPermissionKeysDto, String roleName)
            throws RoleNotFoundException, PermissionNotFound, CannotModifySuperAdminException, ArrayIndexOutOfBoundsException,PermissionToRoleNotFound,IllegalArgumentException {

        for(String permissionKey: listPermissionKeysDto.getPermissionsList()){

            PermissionRoleDto permissionRoleDto=new PermissionRoleDto();
            permissionRoleDto.setRoleName(roleName);
            String[] substrings = permissionKey.split("_");
            permissionRoleDto.setPermissionKey(substrings[0]+"_"+substrings[1]);
            permissionRoleDto.setType(substrings[2]);

            PermissionRoleDto savedPermission=addPermissionToRole(permissionRoleDto);
        }
        return getAllPermissionsOfRole(roleName.toUpperCase());
    }

        @Override
public PermissionRoleDto getPermissionByRole(String permissionKey, String roleName)
        throws PermissionToRoleNotFound, PermissionNotFound, RoleNotFoundException {

    verifyExistPermissionAndRole(permissionKey, roleName);

    Long permissionId=permissionService.getPermissionByKey(permissionKey).getId();
    Long roleId=roleService.getRoleByName(roleName).getId();

    PermissionRole permissionRole = permissionRoleRepository.findByPermissionIdAndRoleId(permissionId, roleId);

    if (permissionRole != null) {
        return permissionRoleFactory.permissionRoleDtoFactory(permissionRole);
    } else {
        throw new PermissionToRoleNotFound("THIS PERMISSION ASSIGNED TO THIS ROLE NOT FOUND");
    }
}

    @Override
    public List<String> getAllPermissionsOfRole(String roleName)
            throws  RoleNotFoundException {

        roleName = roleName.toUpperCase();
        Role role = roleService.getRoleByName(roleName);

        List<PermissionRole> permissionRoles = permissionRoleRepository.findByRoleId(role.getId());

        List<String> permissionKeys = permissionRoles.stream()
                .map(permissionRole -> permissionRole.getPermission().getKey()+"_"+permissionRole.getType())
                .collect(Collectors.toList());

        return permissionKeys;
    }

    @Transactional
    @Override
    public PermissionRoleDto updatePermissionRole(PermissionRoleDto permissionRoleDto) throws PermissionNotFound,
            RoleNotFoundException, PermissionToRoleNotFound {


        String roleName=permissionRoleDto.getRoleName().toUpperCase();
        String permissionKey=permissionRoleDto.getPermissionKey().toUpperCase();
        String type=permissionRoleDto.getType().toUpperCase();

        verifyExistPermissionAndRole(permissionKey,roleName);

      /*  if (roleService.getRoleByName(roleName).getName().equalsIgnoreCase("SUPERADMIN")){
            throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
        }*/

        if(!(type.equals("ALL") || type.equals("ONE"))){
            throw new IllegalArgumentException("TYPE SHOULD BE ALL OR ONE");
        }

        Permission permission = permissionService.getPermissionByKey(permissionKey);
        Role role = roleService.getRoleByName(roleName);


        PermissionRoleDto isExist = getPermissionByRole(permissionKey,roleName);


        PermissionRole permissionRole = permissionRoleRepository.findByPermissionIdAndRoleId(permission.getId(), role.getId());

        permissionRole.setType(type);

        entityManager.merge(permissionRole);
        return getPermissionByRole(permissionKey,roleName);


    }

    @Transactional
    @Override
    public PermissionRoleDto deletePermissionRole(String permissionKey, String roleName) throws PermissionNotFound,
            RoleNotFoundException, PermissionToRoleNotFound,CannotModifySuperAdminException {

        permissionKey= permissionKey.toUpperCase();
        roleName= roleName.toUpperCase();

         /*  if (roleService.getRoleByName(roleName).getName().equalsIgnoreCase("SUPERADMIN")){
            throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
        }*/


        PermissionRoleDto toDeletePermissionDto= getPermissionByRole(permissionKey,roleName);
        PermissionRole toDeletePermission =permissionRoleFactory.permissionRoleFactory(toDeletePermissionDto);
        permissionRoleRepository.delete(toDeletePermission);
        return toDeletePermissionDto;
    }

    @Transactional
    @Override
    public List<String> deleteListePermissionsOfRole(ListPermissionKeysDto listPermissionKeysDto, String roleName) throws PermissionNotFound,
            RoleNotFoundException, PermissionToRoleNotFound ,CannotModifySuperAdminException{

        for(String permissionKey: listPermissionKeysDto.getPermissionsList()){


            String[] substrings = permissionKey.split("_");

            deletePermissionRole(substrings[0]+"_"+substrings[1],roleName);
        }
        return getAllPermissionsOfRole(roleName.toUpperCase());
    }

    @Transactional
    @Override
    public List<String> deleteALLPermissionsOfRole(String roleName) throws PermissionNotFound,
            RoleNotFoundException, PermissionToRoleNotFound ,CannotModifySuperAdminException {

        List<String> allPermissionsToDelete = getAllPermissionsOfRole(roleName);

        ListPermissionKeysDto listPermissionKeysDto = new ListPermissionKeysDto();
        listPermissionKeysDto.setPermissionsList(allPermissionsToDelete);
        List<String> deletePermissions = deleteListePermissionsOfRole(listPermissionKeysDto, roleName);
        return allPermissionsToDelete;
    }
        public void verifyExistPermissionAndRole(String permissionKey, String roleName)
            throws RoleNotFoundException, PermissionNotFound {

        Permission permission = permissionService.getPermissionByKey(permissionKey);
        Role role = roleService.getRoleByName(roleName.toUpperCase());

    }


}
