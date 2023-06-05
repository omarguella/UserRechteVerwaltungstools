package User.Recht.Tool.exception.Permission;

public class EmailAlreadyVerified extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public EmailAlreadyVerified(String reason) {

        this.reason = reason;
    }
}