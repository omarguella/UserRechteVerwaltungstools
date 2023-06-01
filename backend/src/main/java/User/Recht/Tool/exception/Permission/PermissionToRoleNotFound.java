package User.Recht.Tool.exception.Permission;

public class PermissionToRoleNotFound extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PermissionToRoleNotFound(String reason) {

        this.reason = reason;
    }
}