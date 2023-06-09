package User.Recht.Tool.exception.Permission;

public class CannotDeleteInitPermissions extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public CannotDeleteInitPermissions(String reason) {

        this.reason = reason;
    }
}