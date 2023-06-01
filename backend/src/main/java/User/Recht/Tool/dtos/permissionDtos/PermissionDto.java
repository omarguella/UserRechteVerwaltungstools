package User.Recht.Tool.dtos.permissionDtos;

import User.Recht.Tool.entity.Role;

import javax.validation.constraints.NotNull;
import java.util.List;

public class PermissionDto {

    @NotNull
    private String name;

    private List<String> listOfAction;
    private List<Role> roles;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getListOfAction() {
        return listOfAction;
    }

    public void setListOfAction(List<String> listOfAction) {
        this.listOfAction = listOfAction;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }


    @Override
    public String toString() {
        return "PermissionDto{" +
                "name='" + name + '\'' +
                ", listOfAction=" + listOfAction +
                '}';
    }
}

