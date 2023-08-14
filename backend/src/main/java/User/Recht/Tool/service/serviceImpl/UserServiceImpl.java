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

        /** Template for email verification copied from
         * https://unlayer.com/templates
        **/
        String email="<!DOCTYPE HTML PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\n" +
                "<head>\n" +
                "<!--[if gte mso 9]>\n" +
                "<xml>\n" +
                "  <o:OfficeDocumentSettings>\n" +
                "    <o:AllowPNG/>\n" +
                "    <o:PixelsPerInch>96</o:PixelsPerInch>\n" +
                "  </o:OfficeDocumentSettings>\n" +
                "</xml>\n" +
                "<![endif]-->\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "  <meta name=\"x-apple-disable-message-reformatting\">\n" +
                "  <!--[if !mso]><!--><meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\"><!--<![endif]-->\n" +
                "  <title></title>\n" +
                "  \n" +
                "    <style type=\"text/css\">\n" +
                "      @media only screen and (min-width: 620px) {\n" +
                "  .u-row {\n" +
                "    width: 600px !important;\n" +
                "  }\n" +
                "  .u-row .u-col {\n" +
                "    vertical-align: top;\n" +
                "  }\n" +
                "\n" +
                "  .u-row .u-col-100 {\n" +
                "    width: 600px !important;\n" +
                "  }\n" +
                "\n" +
                "}\n" +
                "\n" +
                "@media (max-width: 620px) {\n" +
                "  .u-row-container {\n" +
                "    max-width: 100% !important;\n" +
                "    padding-left: 0px !important;\n" +
                "    padding-right: 0px !important;\n" +
                "  }\n" +
                "  .u-row .u-col {\n" +
                "    min-width: 320px !important;\n" +
                "    max-width: 100% !important;\n" +
                "    display: block !important;\n" +
                "  }\n" +
                "  .u-row {\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "  .u-col {\n" +
                "    width: 100% !important;\n" +
                "  }\n" +
                "  .u-col > div {\n" +
                "    margin: 0 auto;\n" +
                "  }\n" +
                "}\n" +
                "body {\n" +
                "  margin: 0;\n" +
                "  padding: 0;\n" +
                "}\n" +
                "\n" +
                "table,\n" +
                "tr,\n" +
                "td {\n" +
                "  vertical-align: top;\n" +
                "  border-collapse: collapse;\n" +
                "}\n" +
                "\n" +
                "p {\n" +
                "  margin: 0;\n" +
                "}\n" +
                "\n" +
                ".ie-container table,\n" +
                ".mso-container table {\n" +
                "  table-layout: fixed;\n" +
                "}\n" +
                "\n" +
                "* {\n" +
                "  line-height: inherit;\n" +
                "}\n" +
                "\n" +
                "a[x-apple-data-detectors='true'] {\n" +
                "  color: inherit !important;\n" +
                "  text-decoration: none !important;\n" +
                "}\n" +
                "\n" +
                "table, td { color: #000000; } #u_body a { color: #fdc71b; text-decoration: underline; } @media (max-width: 480px) { #u_content_heading_1 .v-font-size { font-size: 33px !important; } #u_content_text_1 .v-container-padding-padding { padding: 40px 10px 10px !important; } #u_content_text_2 .v-container-padding-padding { padding: 8px 10px 40px !important; } }\n" +
                "    </style>\n" +
                "  \n" +
                "  \n" +
                "\n" +
                "<!--[if !mso]><!--><link href=\"https://fonts.googleapis.com/css?family=Montserrat:400,700&display=swap\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]-->\n" +
                "\n" +
                "</head>\n" +
                "\n" +
                "<body class=\"clean-body u_body\" style=\"margin: 0;padding: 0;-webkit-text-size-adjust: 100%;background-color: #ffffff;color: #000000\">\n" +
                "  <!--[if IE]><div class=\"ie-container\"><![endif]-->\n" +
                "  <!--[if mso]><div class=\"mso-container\"><![endif]-->\n" +
                "  <table id=\"u_body\" style=\"border-collapse: collapse;table-layout: fixed;border-spacing: 0;mso-table-lspace: 0pt;mso-table-rspace: 0pt;vertical-align: top;min-width: 320px;Margin: 0 auto;background-color: #ffffff;width:100%\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                "  <tbody>\n" +
                "  <tr style=\"vertical-align: top\">\n" +
                "    <td style=\"word-break: break-word;border-collapse: collapse !important;vertical-align: top\">\n" +
                "    <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td align=\"center\" style=\"background-color: #ffffff;\"><![endif]-->\n" +
                "    \n" +
                "  \n" +
                "  \n" +
                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                "  <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #2f3031;\">\n" +
                "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #2f3031;\"><![endif]-->\n" +
                "      \n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table id=\"u_content_heading_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:30px 10px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <h1 class=\"v-font-size\" style=\"margin: 0px; color: #ffffff; line-height: 140%; text-align: center; word-wrap: break-word; font-family: 'Montserrat',sans-serif; font-size: 31px; font-weight: 400;\"><p><strong>EMAIL VERIFICATION</strong></p>\n" +
                "<p>User-/Rechte-Verwaltungstools</p></h1>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  </div>\n" +
                "  \n" +
                "\n" +
                "\n" +
                "  \n" +
                "  \n" +
                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                "  <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #fbfbfb;\">\n" +
                "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-color: transparent;\">\n" +
                "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-color: #fbfbfb;\"><![endif]-->\n" +
                "      \n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table id=\"u_content_text_1\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:50px 35px 10px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <div class=\"v-font-size\" style=\"font-size: 14px; color: #000000; line-height: 180%; text-align: left; word-wrap: break-word;\">\n" +
                "    <p style=\"font-size: 14px; line-height: 180%;\"><span style=\"font-size: 18px; line-height: 32.4px; color: #000000;\"><strong><span style=\"line-height: 32.4px; font-family: Montserrat, sans-serif; font-size: 18px;\">Dear Mr./Mrs,</span></strong></span></p>\n" +
                "<p style=\"font-size: 14px; line-height: 180%;\"><br /><span style=\"font-size: 18px; line-height: 32.4px;\"><strong><span style=\"color: #222222; font-family: Arial, Helvetica, sans-serif; white-space: normal; background-color: #ffffff; float: none; display: inline; line-height: 25.2px;\">This is your PIN to verify your Email</span></strong></span></p>\n" +

                "<p style=\"font-size: 14px; line-height: 180%;\">"+ pin+ "<br /><br /></p>\n" +
                "<p style=\"font-size: 14px; line-height: 180%;\"><br /><br /></p>\n" +
                "  </div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table id=\"u_content_text_2\" style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:8px 35px 40px 40px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <div class=\"v-font-size\" style=\"font-size: 14px; color: #000000; line-height: 180%; text-align: left; word-wrap: break-word;\">\n" +
                "    <p style=\"line-height: 180%; font-size: 14px;\"><span style=\"font-family: Montserrat, sans-serif; font-size: 16px; line-height: 28.8px;\">If you have any questions, please feel free to contact me directly.</span></p>\n" +
                "<p style=\"font-size: 14px; line-height: 180%;\"><span style=\"font-size: 16px; line-height: 28.8px; font-family: Montserrat, sans-serif;\">Looking forward to your reply,</span></p>\n" +
                "<p style=\"line-height: 180%; font-size: 14px;\"><br /><br /></p>\n" +
                "  </div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:0px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "  <tr>\n" +
                "    <td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\">\n" +
                "      \n" +
                "      \n" +
                "    </td>\n" +
                "  </tr>\n" +
                "</table>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  </div>\n" +
                "  \n" +
                "\n" +
                "\n" +
                "  \n" +
                "  \n" +
                "    <!--[if gte mso 9]>\n" +
                "      <table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;\">\n" +
                "        <tr>\n" +
                "          <td background=\"%20\" valign=\"top\" width=\"100%\">\n" +
                "      <v:rect xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"true\" stroke=\"false\" style=\"width: 600px;\">\n" +
                "        <v:fill type=\"frame\" src=\"%20\" /><v:textbox style=\"mso-fit-shape-to-text:true\" inset=\"0,0,0,0\">\n" +
                "      <![endif]-->\n" +
                "  \n" +
                "<div class=\"u-row-container\" style=\"padding: 0px;background-color: transparent\">\n" +
                "  <div class=\"u-row\" style=\"margin: 0 auto;min-width: 320px;max-width: 600px;overflow-wrap: break-word;word-wrap: break-word;word-break: break-word;background-color: #2f2f2f;\">\n" +
                "    <div style=\"border-collapse: collapse;display: table;width: 100%;height: 100%;background-image: url('%20');background-repeat: no-repeat;background-position: center top;background-color: transparent;\">\n" +
                "      <!--[if (mso)|(IE)]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"padding: 0px;background-color: transparent;\" align=\"center\"><table cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"width:600px;\"><tr style=\"background-image: url('%20');background-repeat: no-repeat;background-position: center top;background-color: #2f2f2f;\"><![endif]-->\n" +
                "      \n" +
                "<!--[if (mso)|(IE)]><td align=\"center\" width=\"600\" style=\"width: 600px;padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\" valign=\"top\"><![endif]-->\n" +
                "<div class=\"u-col u-col-100\" style=\"max-width: 320px;min-width: 600px;display: table-cell;vertical-align: top;\">\n" +
                "  <div style=\"height: 100%;width: 100% !important;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\">\n" +
                "  <!--[if (!mso)&(!IE)]><!--><div style=\"box-sizing: border-box; height: 100%; padding: 0px;border-top: 0px solid transparent;border-left: 0px solid transparent;border-right: 0px solid transparent;border-bottom: 0px solid transparent;border-radius: 0px;-webkit-border-radius: 0px; -moz-border-radius: 0px;\"><!--<![endif]-->\n" +
                "  \n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:0px 10px 20px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "<div align=\"center\">\n" +
                "  <div style=\"display: table; max-width:171px;\">\n" +
                "  <!--[if (mso)|(IE)]><table width=\"171\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td style=\"border-collapse:collapse;\" align=\"center\"><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse; mso-table-lspace: 0pt;mso-table-rspace: 0pt; width:171px;\"><tr><![endif]-->\n" +
                "  \n" +
                "    \n" +
                "    <!--[if (mso)|(IE)]><td width=\"32\" style=\"width:32px; padding-right: 11px;\" valign=\"top\"><![endif]-->\n" +

                "    <!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "    \n" +
                "    \n" +
                "    <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "<table style=\"font-family:arial,helvetica,sans-serif;\" role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" border=\"0\">\n" +
                "  <tbody>\n" +
                "    <tr>\n" +
                "      <td class=\"v-container-padding-padding\" style=\"overflow-wrap:break-word;word-break:break-word;padding:10px 10px 50px;font-family:arial,helvetica,sans-serif;\" align=\"left\">\n" +
                "        \n" +
                "  <div class=\"v-font-size\" style=\"font-size: 14px; color: #ffffff; line-height: 190%; text-align: center; word-wrap: break-word;\">\n" +
                "    <p style=\"font-size: 14px; line-height: 190%;\"><span style=\"font-family: Montserrat, sans-serif; font-size: 14px; line-height: 26.6px;\">If you have any questions, feel free message us at <a rel=\"noopener\" href=\"https://unlayer.com\" target=\"_blank\"><span style=\"text-decoration: underline; font-size: 14px; line-height: 26.6px;\">omarzux@gmail.com</span>.</a> </span><br /><span style=\"font-family: Montserrat, sans-serif; font-size: 14px; line-height: 26.6px;\">All right reserved. Update email preferences or unsubscribe.</span><br /><br /></p>\n" +
                "  </div>\n" +
                "\n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody>\n" +
                "</table>\n" +
                "\n" +
                "  <!--[if (!mso)&(!IE)]><!--></div><!--<![endif]-->\n" +
                "  </div>\n" +
                "</div>\n" +
                "<!--[if (mso)|(IE)]></td><![endif]-->\n" +
                "      <!--[if (mso)|(IE)]></tr></table></td></tr></table><![endif]-->\n" +
                "    </div>\n" +
                "  </div>\n" +
                "  </div>\n" +
                "  \n" +
                "    <!--[if gte mso 9]>\n" +
                "      </v:textbox></v:rect>\n" +
                "    </td>\n" +
                "    </tr>\n" +
                "    </table>\n" +
                "    <![endif]-->\n" +
                "    \n" +
                "\n" +
                "\n" +
                "    <!--[if (mso)|(IE)]></td></tr></table><![endif]-->\n" +
                "    </td>\n" +
                "  </tr>\n" +
                "  </tbody>\n" +
                "  </table>\n" +
                "  <!--[if mso]></div><![endif]-->\n" +
                "  <!--[if IE]></div><![endif]-->\n" +
                "</body>\n" +
                "\n" +
                "</html>\n";

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
        mailer.send(Mail.withHtml(userMail, "EMAIL VERIFICATION FOR YOUR APP", email));
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
        return duplicatedUsers;
    }

}

