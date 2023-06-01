package User.Recht.Tool.dtos.PermissionDtos;

public class PermissionRoleDto {

    private Long id;
    private String permissionKey;
    private String roleName;
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPermissionKey() {
        return permissionKey;
    }

    public void setPermissionKey(String permissionKey) {
        this.permissionKey = permissionKey;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PermissionRole{" +
                "permissionKey='" + permissionKey + '\'' +
                ", roleName='" + roleName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
