package User.Recht.Tool.exception.role;

public class RoleNotAssignedToUserException extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RoleNotAssignedToUserException(String reason) {

        this.reason = reason;
    }
}