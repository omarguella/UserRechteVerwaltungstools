package User.Recht.Tool.exception.user;

public class UserNotFoundException extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UserNotFoundException(String reason) {

        this.reason = reason;
    }
}