package User.Recht.Tool.factory;

import User.Recht.Tool.dtos.UserDto;
import User.Recht.Tool.dtos.UserProfileDto;
import User.Recht.Tool.entity.User;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class UserProfileFactory {

    public User userProfileFactory(UserProfileDto userProfileDto){
        User user=new User();

        if(userProfileDto.getUsername()!=null){
            user.setUsername(userProfileDto.getUsername());
        }
        if(userProfileDto.getName()!=null){
            user.setName(userProfileDto.getName());
        }
        if(userProfileDto.getLastName()!=null){
            user.setLastName(userProfileDto.getLastName());
        }
        if(userProfileDto.getPhoneNumber()!=null){
            user.setPhoneNumber(userProfileDto.getPhoneNumber());
        }
        return user;
    }
}
