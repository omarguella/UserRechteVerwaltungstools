package User.Recht.Tool.dtos.permissionDtos;


import java.util.List;

public class ListPermissionKeysDto {
    List<String> permissionsList;

    public List<String> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<String> permissionsList) {
        this.permissionsList = permissionsList;
    }

    @Override
    public String toString() {
        return "addPermissionRoleDto{" +
                "permissionsList=" + permissionsList +
                '}';
    }
}
