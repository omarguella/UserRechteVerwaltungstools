package User.Recht.Tool.factory.userFactorys;

import User.Recht.Tool.dtos.userDtos.UserDto;
import User.Recht.Tool.dtos.userDtos.UserProfileDto;
import User.Recht.Tool.entity.User;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class UserFactory {

    public User userFactory(UserDto userDto){
        User user=new User();
        user.setEmail(userDto.getEmail().toUpperCase());
        user.setName(userDto.getName());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername().toUpperCase());
        user.setPassword(userDto.getPassword());
        user.setIsVerifiedEmail(userDto.getIsVerifiedEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setRoles(userDto.getRoles());
        return user;
    }

    public User userUpdateProfileFactory(User user, UserProfileDto userProfileDto){

        if(userProfileDto.getUsername()!=null){
            user.setUsername(userProfileDto.getUsername().toUpperCase());
        }
        if(userProfileDto.getName()!=null){
            user.setName(userProfileDto.getName());
        }
        if(userProfileDto.getLastname()!=null){
            user.setLastname(userProfileDto.getLastname());
        }
        if(userProfileDto.getPhoneNumber()!=null){
            user.setPhoneNumber(userProfileDto.getPhoneNumber());
        }
        return user;
    }
}
