package User.Recht.Tool.exception.role;

public class RoleNotFoundException extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public RoleNotFoundException(String reason) {

        this.reason = reason;
    }
}