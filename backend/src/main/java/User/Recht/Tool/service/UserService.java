package User.Recht.Tool.service;


import User.Recht.Tool.entity.User;
import User.Recht.Tool.dtos.UserDto;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.UserNotFoundException;

import javax.transaction.Transactional;
import java.util.List;

public interface UserService {
    User createUser(UserDto user) throws DuplicateElementException, Exception;

    User getUserById(long id) throws UserNotFoundException;

    List<User> getAllUsers();
    User getUserByUsername(String username) throws UserNotFoundException;

    User getUserByEmail(String email) throws UserNotFoundException;

    User saveUser(UserDto user) throws Exception;

    User deleteUserByEmail(String email) throws Exception;

    Boolean verifyPasswordByEmail(String oldPassword, String email) throws UserNotFoundException;
    void updatePasswordByEmail(String password, String email) throws UserNotFoundException;

    User saveUpdatedUser(User user);

    User updateEmailUser(String currentEmail, String newEmail) throws UserNotFoundException;



}