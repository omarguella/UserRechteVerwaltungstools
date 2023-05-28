package User.Recht.Tool.exception.superadmin;

public class CannotModifySuperAdminException extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public CannotModifySuperAdminException(String reason) {

        this.reason = reason;
    }
}