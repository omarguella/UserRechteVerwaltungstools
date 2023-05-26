package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.UpdatePasswordDto;
import User.Recht.Tool.dtos.UserDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.UserNameDuplicateElementException;
import User.Recht.Tool.exception.UserNotFoundException;
import User.Recht.Tool.factory.UserFactory;
import User.Recht.Tool.repository.UserRepository;
import User.Recht.Tool.service.UserService;
import User.Recht.Tool.util.Encoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.List;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[.@$!%*?&])[A-Za-z\\d.@$!%*?&]{8,}$";


    @Transactional
    @Override
    public User createUser(UserDto userDto) throws DuplicateElementException, Exception {


        if (userDto.getEmail().isBlank() || userDto.getPassword().isBlank() || userDto.getUserName().isBlank()) {
            throw new NullPointerException("Email , Password and Username are Required");
        }

        userDto.setEmail(userDto.getEmail().toUpperCase());

        if (!isValidEmail(userDto.getEmail()) || !isValidPassword(userDto.getPassword())) {
            throw new ValidationException("THE EMAIL OR THE PASSWORD IS NOT VALID");
        }


        userDto.setUserName(userDto.getUserName().toUpperCase());

        try {
            User userCheckWithEmail = getUserByEmail(userDto.getEmail());
            throw new DuplicateElementException("User Email " + userDto.getEmail() + " existed");
        } catch (UserNotFoundException e) {
        }

        try {
            User userCheckWithUsername = getUserByUsername(userDto.getUserName());
            throw new UserNameDuplicateElementException("Username " + userDto.getUserName() + " existed");
        } catch (UserNotFoundException e) {
        }

        userDto.setPassword(passwordEncoder.encode((userDto.getPassword())));

        return saveUser(userDto);


    }

    @Transactional
    @Override
    public User saveUser(UserDto userDto) throws Exception {
        User user = new User();
        user = userFactory.userFactory(userDto);
        userRepository.persistAndFlush(user);
        return getUserByEmail(user.getEmail());
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
    public List<User> getAllUsers() {
        return userRepository.listAll();

    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        User user = userRepository.find("email", email).firstResult();
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("USER DONT EXIST");

        }
    }

    @Override
    public User getUserByUsername(String username) throws UserNotFoundException {
        User user = userRepository.find("username", username).firstResult();
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("USER DONT EXIST");
        }

    }

    @Transactional
    @Override
    public User updateEmailUser(Long id, String newEmail) throws UserNotFoundException, ValidationException {

        User userToUpdate;

        try {
            userToUpdate = getUserById(id);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("USER DONT EXIST");
        }
        if (!isValidEmail(newEmail)) {
            throw new ValidationException("the New Mail is not valid");
        } else if (StringUtils.isNotBlank(newEmail)) {
            userToUpdate.setEmail(newEmail);
        }
        userToUpdate.setIsVerifiedEmail(false);
        return saveUpdatedUser(userToUpdate);
    }

    @Transactional
    public User saveUpdatedUser(User user) {
        userRepository.getEntityManager().merge(user);
        return user;
    }


    @Transactional
    @Override
    public User deleteUserById(Long id) throws UserNotFoundException {
        try {
            User user = getUserById(id);
            userRepository.delete(user);
            //logout all
            return user;
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("USER DONT EXIST");
        }
    }


    @Transactional
    @Override
    public User updatePasswordById(Long id, UpdatePasswordDto updatePasswordDto) throws UserNotFoundException, ValidationException ,IllegalArgumentException{

        User userToUpdate;

        try {
            userToUpdate = getUserById(id);
        } catch (UserNotFoundException e) {
            throw new UserNotFoundException("USER DONT EXIST");
        }

        if (!verifyPasswordById(updatePasswordDto.getOldPassword(),id)) {
            throw new IllegalArgumentException("OLD PASSWORD IS WRONG");
        } else if (!isValidPassword(updatePasswordDto.getNewPassword())) {
            throw new ValidationException(" NEW PASSWORD IS NOT VALID");
        }

        userToUpdate.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        return saveUpdatedUser(userToUpdate);
    }


    public Boolean verifyPasswordById(String password, Long id) throws UserNotFoundException {
        User user = getUserById(id);
        return passwordEncoder.encode(password).equals(user.getPassword());

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

}

