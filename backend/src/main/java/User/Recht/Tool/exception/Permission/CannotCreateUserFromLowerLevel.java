package User.Recht.Tool.exception.Permission;

public class CannotCreateUserFromLowerLevel extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public CannotCreateUserFromLowerLevel(String reason) {

        this.reason = reason;
    }
}