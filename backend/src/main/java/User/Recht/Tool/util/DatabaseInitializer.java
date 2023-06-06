package User.Recht.Tool.util;

import User.Recht.Tool.entity.Permission;
import User.Recht.Tool.entity.PermissionRole;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.repository.PermissionRepository;
import User.Recht.Tool.repository.PermissionRoleRepository;
import User.Recht.Tool.repository.RoleRepository;
import User.Recht.Tool.repository.UserRepository;
import User.Recht.Tool.resource.UserResource;
import io.quarkus.runtime.StartupEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class DatabaseInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Inject
    RoleRepository roleRepository;
    @Inject
    UserRepository userRepository;
    @Inject
    PermissionRoleRepository permissionRoleRepository;
    @Inject
    PermissionRepository permissionRepository;
    @Inject
    Encoder encoder;

    @Transactional
    void onStart(@Observes StartupEvent event) {
        if (roleRepository.findAll().list().isEmpty()) {

            Role role = new Role();
            role.setLevel(0);
            role.setName("SUPERADMIN");
            role.setIsPrivate(true);
            role.setSessionTimer(0L);
            role.setIsMailToVerify(false);
            roleRepository.persist(role);
        }
        if (userRepository.findAll().list().isEmpty()) {
            User user = new User();
            user.setEmail("SUPERADMIN@EMAIL.COM");
            user.setUsername("SUPERADMIN");
            user.setPassword(encoder.passwordCoder("1234"));
            user.setIsVerifiedEmail(true);
            List<Role> roles = roleRepository.find("name", "SUPERADMIN").list();
            user.setRoles(roles);
            user.setName("SUPERADMIN");
            user.setLastname("SUPERADMIN");
            user.setPhoneNumber("0000000");
            userRepository.persist(user);
        }
        if (permissionRepository.findAll().list().isEmpty()) {

            Permission permission;
            PermissionRole permissionRole ;
            Role role = roleRepository.find("name", "SUPERADMIN").firstResult();



            // USER_MANAGER

            permission = new Permission();
            permission.setKey("USER_MANAGER_GET");
            permission.setName("USER_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            permission = new Permission();
            permission.setKey("USER_MANAGER_POST");
            permission.setName("USER_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            permission = new Permission();
            permission.setKey("USER_MANAGER_DELETE");
            permission.setName("USER_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            permission = new Permission();
            permission.setKey("USER_MANAGER_PUT");
            permission.setName("USER_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            // ROLE_MANAGER

            permission = new Permission();
            permission.setKey("ROLE_MANAGER_GET");
            permission.setName("ROLE_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            permission = new Permission();
            permission.setKey("ROLE_MANAGER_POST");
            permission.setName("ROLE_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            permission = new Permission();
            permission.setKey("ROLE_MANAGER_DELETE");
            permission.setName("ROLE_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            permission = new Permission();
            permission.setKey("ROLE_MANAGER_PUT");
            permission.setName("ROLE_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            // PERMISSION_MANAGER
            permission = new Permission();
            permission.setKey("PERMISSION_MANAGER_GET");
            permission.setName("PERMISSION_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            permission = new Permission();
            permission.setKey("PERMISSION_MANAGER_POST");
            permission.setName("PERMISSION_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            permission = new Permission();
            permission.setKey("PERMISSION_MANAGER_DELETE");
            permission.setName("PERMISSION_MANAGER");
            permissionRepository.persist(permission);

            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);

            permission = new Permission();
            permission.setKey("PERMISSION_MANAGER_PUT");
            permission.setName("PERMISSION_MANAGER");
            permissionRepository.persist(permission);


            permission=permissionRepository.find("key",permission.getKey()).firstResult();
            permissionRole = new PermissionRole();
            permissionRole.setRole(role);
            permissionRole.setPermission(permission);
            permissionRole.setType("ALL");
            permissionRoleRepository.persist(permissionRole);


        }

        LOGGER.info("SUPERADMIN IS INITIALISED");
    }

}
