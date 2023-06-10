package User.Recht.Tool.exception.Permission;

public class EmailNotVerified extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public EmailNotVerified(String reason) {

        this.reason = reason;
    }
}