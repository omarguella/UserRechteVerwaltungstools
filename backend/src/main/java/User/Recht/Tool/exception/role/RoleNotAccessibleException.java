package User.Recht.Tool.exception.role;

public class RoleNotAccessibleException extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RoleNotAccessibleException(String reason) {

        this.reason = reason;
    }
}