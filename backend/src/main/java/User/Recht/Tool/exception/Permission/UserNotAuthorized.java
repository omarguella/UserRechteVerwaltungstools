package User.Recht.Tool.exception.Permission;

public class UserNotAuthorized extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UserNotAuthorized(String reason) {

        this.reason = reason;
    }
}