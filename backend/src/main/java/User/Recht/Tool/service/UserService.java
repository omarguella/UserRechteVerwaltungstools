package User.Recht.Tool.service;


import User.Recht.Tool.dtos.UpdatePasswordDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.dtos.UserDto;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.UserNotFoundException;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.List;

public interface UserService {
    User createUser(UserDto user) throws DuplicateElementException, Exception;

    User getUserById(long id) throws UserNotFoundException;

    List<User> getAllUsers();
    User getUserByUsername(String username) throws UserNotFoundException;

    User getUserByEmail(String email) throws UserNotFoundException;

    User saveUser(UserDto user) throws Exception;



    @Transactional
    User deleteUserById(Long id) throws UserNotFoundException;

    @Transactional
    User updateEmailUser(Long id, String newEmail) throws UserNotFoundException, ValidationException;

    User saveUpdatedUser(User user);


    @Transactional
    User updatePasswordById(Long id, UpdatePasswordDto updatePasswordDto) throws UserNotFoundException, ValidationException ,IllegalArgumentException;
}