package User.Recht.Tool.exception.Permission;

public class SessionTimeoutException extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public SessionTimeoutException(String reason) {

        this.reason = reason;
    }
}