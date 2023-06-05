package User.Recht.Tool.util;

import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
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

        LOGGER.info("SUPERADMIN IS INITIALISED");
    }

}
