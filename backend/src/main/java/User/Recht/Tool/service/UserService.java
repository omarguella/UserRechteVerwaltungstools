package User.Recht.Tool.service;


import User.Recht.Tool.dtos.userDtos.UpdatePasswordDto;
import User.Recht.Tool.dtos.userDtos.UserProfileDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.dtos.userDtos.UserDto;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.util.List;

public interface UserService {
    User createUser(UserDto user,String name) throws DuplicateElementException, NullPointerException, ValidationException, RoleNotFoundException, UserNotFoundException;

    User getUserById(long id) throws UserNotFoundException;

    List<User> getAllUsers();
    User getUserByUsername(String username) throws UserNotFoundException;

    List<User> getAllUsersByRole(String roleName) throws  RoleNotFoundException;

    User getUserByEmail(String email) throws UserNotFoundException;


    @Transactional
    User deleteUserById(Long id) throws UserNotFoundException, CannotModifySuperAdminException;

    @Transactional
    User updateEmailUser(Long id, String newEmail) throws UserNotFoundException,DuplicateElementException, ValidationException;

    User saveUpdatedUser(User user);

    @Transactional
    User updatePasswordById(Long id, UpdatePasswordDto updatePasswordDto) throws UserNotFoundException, ValidationException ,IllegalArgumentException;

    @Transactional
    User updateProfilById(Long id, UserProfileDto userProfileDto) throws CannotModifySuperAdminException,UserNotFoundException,ValidationException, DuplicateElementException;
}