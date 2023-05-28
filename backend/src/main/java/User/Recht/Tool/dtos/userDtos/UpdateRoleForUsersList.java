package User.Recht.Tool.dtos.userDtos;

import java.util.List;

public class UpdateRoleForUsersList {

    String action;
    List<Long> usersIdList;
    String deleteRole;
    String movedTo;
    String addRole;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public List<Long> getUsersIdList() {
        return usersIdList;
    }

    public void setUsersIdList(List<Long> usersIdList) {
        this.usersIdList = usersIdList;
    }

    public String getDeleteRole() {
        return deleteRole;
    }

    public void setDeleteRole(String deleteRole) {
        this.deleteRole = deleteRole;
    }

    public String getMovedTo() {
        return movedTo;
    }

    public void setMovedTo(String movedTo) {
        this.movedTo = movedTo;
    }

    public String getAddRole() {
        return addRole;
    }

    public void setAddRole(String addRole) {
        this.addRole = addRole;
    }
}
