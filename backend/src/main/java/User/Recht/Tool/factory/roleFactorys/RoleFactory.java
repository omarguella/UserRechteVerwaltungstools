package User.Recht.Tool.factory.roleFactorys;

import User.Recht.Tool.dtos.roleDtos.RoleDto;
import User.Recht.Tool.dtos.roleDtos.UpdateRoleDto;
import User.Recht.Tool.entity.Role;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class RoleFactory {


        public  Role roleFactory(RoleDto roleDto){
            Role role=new Role();
            role.setName(roleDto.getName());
            role.setIsMailToVerify(roleDto.getIsMailToVerify());
            role.setSessionTimer(roleDto.getSessionTimer());
            role.setTowFactorAuth(roleDto.getTowFactorAuth());
            role.setIsPrivate(roleDto.getIsPrivate());
            return role;
        }

    public Role updateRoleFactory(Role role, UpdateRoleDto updateRoleDto){

        if(updateRoleDto.getName()!=null){
            role.setName(updateRoleDto.getName().toUpperCase());
        }
        if(updateRoleDto.getSessionTimer()!=null){
            role.setSessionTimer(updateRoleDto.getSessionTimer());
        }
        role.setIsMailToVerify(updateRoleDto.getIsMailToVerify());
        role.setTowFactorAuth(updateRoleDto.getTowFactorAuth());
        role.setIsPrivate(updateRoleDto.getIsPrivate());
        return role;
    }
}
