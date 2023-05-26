package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.UserDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.UserNotFoundException;
import User.Recht.Tool.exception.userNameDuplicateElementException;
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
import javax.ws.rs.NotFoundException;
import java.util.List;

@RequestScoped
public class UserServiceImpl implements UserService {
    @Inject
    UserRepository userRepository;
    @Inject
    Encoder passwordEncoder;
    @Inject
    UserFactory userFactory;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);



    @Transactional
    @Override
    public User createUser(UserDto userDto) throws DuplicateElementException, Exception {



       /*if ((!(user.getType().equalsIgnoreCase("ADMIN"))) && (!(user.getType().equalsIgnoreCase("PERSON")))) {
            throw new DuplicateElementException("Role should be ADMIN or PERSON");
        }*/

        if (userDto.getEmail().isBlank() || userDto.getPassword().isBlank() || userDto.getUserName().isBlank()) {
            throw new NullPointerException("Email , Password and Username are Required");
        }

        userDto.setEmail(userDto.getEmail().toUpperCase());
        User userCheckWithEmail = getUserByEmail(userDto.getEmail());
        User userCheckWithUsername = getUserByUsername(userDto.getUserName());


        if (userCheckWithEmail != null) {
            throw new DuplicateElementException("User Email " + userDto.getEmail() + " existed");
        } else if (userCheckWithUsername != null) {
            throw new userNameDuplicateElementException("This Username " + userDto.getEmail() + " existed");
        } else {
            //FOR ROLE
            //user.setType(user.getType().toUpperCase());
            userDto.setPassword(passwordEncoder.encode((userDto.getPassword())));

            return saveUser(userDto);
        }
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

        try {
            return userRepository.findById(id);
        } catch (NotFoundException e) {
            throw new UserNotFoundException(" USER DON´T EXIST");
        }
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.listAll();

    }

    @Override
    public User getUserByEmail(String email) throws UserNotFoundException {
        try {
            return userRepository.find("email", email).firstResult();
        } catch (NotFoundException e) {
            throw new UserNotFoundException(" USER DON´T EXIST");
        }

    }

    @Override
    public User getUserByUsername(String username) throws UserNotFoundException {
        try {
            return userRepository.find("username", username).firstResult();
        } catch (NotFoundException e) {
            throw new UserNotFoundException(" USER DON´T EXIST");
        }

    }

    @Transactional
    @Override
    public User updateEmailUser(String currentEmail, String newEmail) throws UserNotFoundException {
        User userToUpdate = getUserByEmail(currentEmail);
        if (StringUtils.isNotBlank(newEmail)) {
            userToUpdate.setEmail(newEmail);
        }
        return saveUpdatedUser(userToUpdate);
    }

    @Transactional
    public User saveUpdatedUser(User user) {
        userRepository.getEntityManager().merge(user);
        return user;
    }


    @Transactional
    @Override
    public User deleteUserByEmail(String email) throws Exception {
        User user = getUserByEmail(email);
        userRepository.delete(user);
        //Boolean logOut = authorizationService.logoutAll(user.getId());
        return user;

    }

    @Transactional
    @Override
    public void updatePasswordByEmail(String password, String email) throws UserNotFoundException {
        User user = getUserByEmail(email);
        //user.setPassword(passwordEncoder.encode(password));
        user.setPassword(password);
        saveUpdatedUser(user);
    }

    @Override
    public Boolean verifyPasswordByEmail(String password, String email) throws UserNotFoundException {
        User user = getUserByEmail(email);
        //return passwordEncoder.encode(password).equals(user.getPassword());
        return password.equals(user.getPassword());

    }

   /* private User createEntity(UserDTO userDTO) {
        // generate new user entity
        User user = new User();
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setType(userDTO.getType());
        user.setId(userDTO.getId());
        return user;
    }*/


}

