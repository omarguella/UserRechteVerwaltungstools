package User.Recht.Tool.exception.Permission;

public class PermissionNotFound extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PermissionNotFound(String reason) {

        this.reason = reason;
    }
}