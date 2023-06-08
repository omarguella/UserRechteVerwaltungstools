package User.Recht.Tool.dtos.permissionDtos;


import java.util.List;

public class ListPermissionKeysDto {
    List<String> permissionsList;

    String roleName;
    public List<String> getPermissionsList() {
        return permissionsList;
    }

    public void setPermissionsList(List<String> permissionsList) {
        this.permissionsList = permissionsList;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "addPermissionRoleDto{" +
                "permissionsList=" + permissionsList +
                "roleName=" + roleName +
                '}';
    }
}
