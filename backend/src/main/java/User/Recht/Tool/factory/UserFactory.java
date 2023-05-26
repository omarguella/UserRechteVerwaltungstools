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
        user.setUserName(userDto.getUserName());
        user.setPassword(userDto.getPassword());
        return user;
    }
}
