package User.Recht.Tool.factory;

import User.Recht.Tool.dtos.UserDto;
import User.Recht.Tool.entity.User;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class UserFactory {

    public User userFactory(UserDto userDto){
        User user=new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(userDto.getPassword());
        user.setIsVerifiedEmail(userDto.getIsVerifiedEmail());
        user.setPhoneNumber(userDto.getPhoneNumber());
        return user;
    }
}
