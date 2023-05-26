package User.Recht.Tool.factory;

import User.Recht.Tool.dtos.UserDto;
import User.Recht.Tool.dtos.UserProfileDto;
import User.Recht.Tool.entity.User;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class UserProfileFactory {

    public User userProfileFactory(User user,UserProfileDto userProfileDto){

        if(userProfileDto.getUsername()!=null){
            user.setUsername(userProfileDto.getUsername());
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
