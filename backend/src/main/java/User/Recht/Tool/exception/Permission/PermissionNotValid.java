package User.Recht.Tool.exception.Permission;

public class PermissionNotValid extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PermissionNotValid(String reason) {

        this.reason = reason;
    }
}