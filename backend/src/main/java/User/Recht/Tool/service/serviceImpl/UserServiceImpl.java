package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.userDtos.PinVerifyDto;
import User.Recht.Tool.dtos.userDtos.UpdatePasswordDto;
import User.Recht.Tool.dtos.userDtos.UserDto;
import User.Recht.Tool.dtos.userDtos.UserProfileDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.Permission.CannotCreateUserFromLowerLevel;
import User.Recht.Tool.exception.Permission.EmailAlreadyVerified;
import User.Recht.Tool.exception.Permission.PinNotFound;
import User.Recht.Tool.exception.role.RoleMovedToException;
import User.Recht.Tool.exception.role.RoleNotAccessibleException;
import User.Recht.Tool.exception.role.RoleNotAssignedToUserException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNameDuplicateElementException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.factory.userFactorys.UserFactory;
import User.Recht.Tool.repository.UserRepository;
import User.Recht.Tool.service.RoleService;
import User.Recht.Tool.service.RoleToUserService;
import User.Recht.Tool.service.UserService;
import User.Recht.Tool.util.Encoder;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class UserServiceImpl implements UserService {
    @Inject
    UserRepository userRepository;
    @Inject
    Encoder passwordEncoder;
    @Inject
    UserFactory userFactory;
    @Inject
    RoleService roleService;
    @Inject
    RoleToUserService roleToUserService;
    @PersistenceContext
    EntityManager entityManager;

    @Inject
    Mailer mailer;


    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[.@$!%*?&])[A-Za-z\\d.@$!%*?&]{8,}$";
    private static final String PHONE_REGEX = "^(\\+[0-9]{1,3})?[0-9]{9,15}$";


    @Transactional
    @Override
    public User createPrivateUser(UserDto userDto, String roleName, User user)
            throws DuplicateElementException, NullPointerException, ValidationException,
            RoleNotFoundException, UserNotFoundException, CannotCreateUserFromLowerLevel {


        int minRoleLevel;

        List<Role> roles = roleService.getAvailibaleRoles(user);

        Role role = roleService.getRoleByName(roleName);

        if (roles.contains(role)) {
            return createUser(userDto, roleName);
        } else {
            throw new CannotCreateUserFromLowerLevel("CANNOT CREATE A USER FROM A HIGHER ROLE LEVEL");
        }
    }

    @Transactional
    @Override
    public User createUser(UserDto userDto, String roleName) throws DuplicateElementException, NullPointerException, ValidationException, RoleNotFoundException, UserNotFoundException {

        /* after INIT
        if(roleName.toUpperCase().equals("SUPERADMIN")){
            throw new CannotModifySuperAdminException("CANNOT ADD A SUPERADMIN USER");
        }*/

        userDto.setRoles(new ArrayList<>());

        if (userDto.getEmail().isBlank() || userDto.getPassword().isBlank() || userDto.getUsername().isBlank()
                || userDto.getName().isBlank() || userDto.getLastname().isBlank()) {
            throw new NullPointerException("Email , Password, Username, Name, LastName are Required");
        }


        if (!isValidEmail(userDto.getEmail()) || !isValidPassword(userDto.getPassword()) || !isValidPhone(userDto.getPhoneNumber())) {
            throw new ValidationException(" EMAIL, PASSWORD OR PHONENUMBER IS NOT VALID");
        }

        userDto.setEmail(userDto.getEmail().toUpperCase());
        userDto.setUsername(userDto.getUsername().toUpperCase());

        try {
            User userCheckWithEmail = getUserByEmail(userDto.getEmail());
            throw new DuplicateElementException("User Email " + userDto.getEmail() + " existed");
        } catch (UserNotFoundException ignored) {
        }

        try {
            User userCheckWithUsername = getUserByUsername(userDto.getUsername());
            throw new UserNameDuplicateElementException("Username " + userDto.getUsername() + " existed");
        } catch (UserNotFoundException ignored) {
        }

        userDto.setPassword(passwordEncoder.passwordCoder((userDto.getPassword())));

        userDto = assignRoleToUser(userDto, roleName);

        saveUser(userDto);
        return getUserByEmail(userDto.getEmail());

    }

    @Transactional
    public void saveUser(UserDto userDto) {
        User user = userFactory.userFactory(userDto);
        userRepository.persist(user);
    }

    @Override
    public User getUserById(long id) throws UserNotFoundException {

        User user = userRepository.findById(id);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("USER DONT EXIST");
        }

    }

    @Override
    public List<User> getAllUsers(User user, String token) throws RoleNotFoundException, RoleNotAccessibleException {

        List<Role> availableRoles = roleService.getAvailibaleRolesToEdit(user, token);
        List<User> allAvailableUsers = new ArrayList<>();

        for (Role role : availableRoles) {
            allAvailableUsers.addAll(getAllUsersByRole(user, token, role.getName()));
        }

        return allAvailableUsers;

    }

    @Override
    public List<User> getAllUsersByRole(User user, String token, String roleName) throws RoleNotFoundException, RoleNotAccessibleException {

        List<Role> availableRoles = roleService.getAvailibaleRolesToEdit(user, token);
        Role checkRole = roleService.getRoleByName(roleName);

        if (!availableRoles.contains(checkRole)) {
            throw new RoleNotAccessibleException("Role not available to the User");
        }

        roleName = roleName.toUpperCase();
        roleService.getRoleByName(roleName);
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName", User.class);
        query.setParameter("roleName", roleName);

        return query.getResultList();
    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.find("email", email.toUpperCase()).firstResult();
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("USER DONT EXIST");

        }
    }

    @Override
    public User getUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.find("username", username.toUpperCase()).firstResult();
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("USER DONT EXIST");
        }

    }

    /*getUsersByRole*/

    @Transactional
    @Override
    public User updateEmailUser(Long id, String newEmail) throws UserNotFoundException, ValidationException, DuplicateElementException {

        User userToUpdate = getUserById(id);

        if (!isValidEmail(newEmail)) {
            throw new ValidationException("the New Mail is not valid");
        } else {
            try {
                User checkUserEmail = getUserByEmail(newEmail.toUpperCase());
                if (checkUserEmail.getId() != id) {
                    throw new DuplicateElementException("USER EMAIL ALREADY USED");
                }
            } catch (UserNotFoundException e) {

            }
        }
        Hibernate.initialize(userToUpdate.getRoles());
        userToUpdate.setEmail(newEmail.toUpperCase());
        userToUpdate.setIsVerifiedEmail(false);
        return saveUpdatedUser(userToUpdate);
    }

    @Transactional
    @Override
    public User updatePasswordById(User user, Long id, UpdatePasswordDto updatePasswordDto) throws UserNotFoundException, ValidationException, IllegalArgumentException {

        User userToUpdate = getUserById(id);

        if (id == user.getId()) {
            if (!verifyPasswordById(updatePasswordDto.getOldPassword(), id)) {
                throw new IllegalArgumentException("OLD PASSWORD IS WRONG");
            }
        }
        if (!isValidPassword(updatePasswordDto.getNewPassword())) {
            throw new ValidationException(" NEW PASSWORD IS NOT VALID");
        }

        userToUpdate.setPassword(passwordEncoder.passwordCoder(updatePasswordDto.getNewPassword()));
        Hibernate.initialize(userToUpdate.getRoles());
        return saveUpdatedUser(userToUpdate);
    }

    @Transactional
    @Override
    public User updateProfilById(Long id, UserProfileDto userProfileDto) throws UserNotFoundException, CannotModifySuperAdminException, ValidationException, DuplicateElementException, RoleNotFoundException, RoleMovedToException, RoleNotAssignedToUserException {

        User userToUpdate = getUserById(id);


        if (userProfileDto.getUsername() != null) {
            if (getUserById(id).getUsername().equals("SUPERADMIN")) {
                // throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
            }
            try {
                userProfileDto.setUsername(userProfileDto.getUsername().toUpperCase());
                User checkUsername = getUserByUsername(userProfileDto.getUsername());
                if (checkUsername.getId() != id) {
                    throw new DuplicateElementException("USERNAME ALREADY USED");
                }
            } catch (UserNotFoundException e) {

            }
        }

        if (userProfileDto.getPhoneNumber() != null) {
            if (!isValidPhone(userProfileDto.getPhoneNumber())) {
                throw new ValidationException(" PHONENUMBER IS NOT VALID");
            }
        }

        if (userProfileDto.getRoles() != null) {
            while (userToUpdate.getRoles().size() > 0) {
                roleToUserService.deleteRoleFromUser(userToUpdate.getId(), userToUpdate.getRoles().get(userToUpdate.getRoles().size() - 1)
                        .getName(), userProfileDto.getRoles().get(0));
                userToUpdate.getRoles().remove(userToUpdate.getRoles().size() - 1);
            }

            for (String role : userProfileDto.getRoles()) {
                roleToUserService.addRoleToUser(userToUpdate.getId(), role);
            }

        }

        if (userProfileDto.getEmail() != null) {
            updateEmailUser(userToUpdate.getId(), userProfileDto.getEmail());
        }

        userToUpdate = userFactory.userUpdateProfileFactory(userToUpdate, userProfileDto);
        Hibernate.initialize(userToUpdate.getRoles());
        return saveUpdatedUser(userToUpdate);
    }

    @Transactional
    @Override
    public User deleteUserById(Long id) throws UserNotFoundException, CannotModifySuperAdminException {

        if (getUserById(id).getUsername().equals("SUPERADMIN")) {
            throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
        }

        try {
            User user = getUserById(id);
            deleteAllRolesFromUser(user);
            //logout all
            return user;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("USER DONT EXIST");
        }
    }

    @Transactional
    @Override
    public void sendPinForEmailVerify(User user) {
        String userMail = user.getEmail();
        String pin = generateRandomDigits(5);
        user.setPinEmail(passwordEncoder.passwordCoder(pin));
        user = saveUpdatedUser(user);

        String body = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<h1><strong>EMAIL VERIFICATION </strong></h1>\n" +
                "<p>This is your PIN to verify your Email\n </p>" +
                " <div>" + pin + " </div>  \n" +
                "<div> Thanks! </div>  \n" +
                " \n" +
                " \n" +
                "</body>\n" +
                "</html>\n";
        mailer.send(Mail.withHtml(userMail, "EMAIL VERIFICATION FOR YOUR APP", body));
    }

    @Transactional
    @Override
    public User emailVerifyByPin(User user, PinVerifyDto pinVerify) throws PinNotFound, EmailAlreadyVerified {
        if (user.getIsVerifiedEmail()) {
            throw new EmailAlreadyVerified("EMAIL IS ALREADY VERIFIED");
        }
        String pin = passwordEncoder.passwordCoder(pinVerify.getPin());
        if (!pin.equals(user.getPinEmail())) {
            throw new PinNotFound("PIN IS WRONG");
        } else {
            user.setIsVerifiedEmail(true);
            return saveUpdatedUser(user);
        }
    }


    public Boolean verifyPasswordById(String password, Long id) throws UserNotFoundException {
        User user = getUserById(id);
        return passwordEncoder.passwordCoder(password).equals(user.getPassword());

    }

    @Transactional
    public User saveUpdatedUser(User user) {
        userRepository.getEntityManager().merge(user);
        return user;
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public static boolean isValidPhone(String phoneNumber) {
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    @Transactional
    public UserDto assignRoleToUser(UserDto userDto, String roleName) throws RoleNotFoundException {
        roleName = roleName.toUpperCase();
        Role role = roleService.getRoleByName(roleName);
        userDto.getRoles().add(role);
        return userDto;
    }


    @Transactional
    public void deleteAllRolesFromUser(User user) {
        user.setRoles(null);
        user = saveUpdatedUser(user);
        userRepository.delete(user);
    }

    public static String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int digit = random.nextInt(10); // Generates a random digit between 0 and 9
            sb.append(digit);
        }

        return sb.toString();
    }

    @Override
    public List<User> performanceTest() {

        List<User> users = userRepository.listAll();
        List<User> duplicatedUsers = new ArrayList<>(users);
        while (duplicatedUsers.size() < 100) {
            duplicatedUsers.addAll(users);
        }
        LOGGER.info(String.valueOf(duplicatedUsers.size()));
        return duplicatedUsers;
    }

}

