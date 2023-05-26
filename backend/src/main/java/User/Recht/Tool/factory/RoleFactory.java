package User.Recht.Tool.factory;

import User.Recht.Tool.dtos.RoleDto;
import User.Recht.Tool.dtos.UserDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class RoleFactory {


        public  Role roleFactory(RoleDto roleDto){
            Role role=new Role();
            role.setName(roleDto.getName());
            role.setIsMailToVerify(roleDto.getIsMailToVerify());
            role.setSessionTimer(roleDto.getSessionTimer());
            role.setTowFactorAuth(roleDto.getTowFactorAuth());
            return role;
        }

}
