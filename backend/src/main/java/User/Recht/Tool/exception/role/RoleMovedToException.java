package User.Recht.Tool.exception.role;

public class RoleMovedToException extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RoleMovedToException(String reason) {

        this.reason = reason;
    }
}