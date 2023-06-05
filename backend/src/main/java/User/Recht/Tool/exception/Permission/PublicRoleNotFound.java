package User.Recht.Tool.exception.Permission;

public class PublicRoleNotFound extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PublicRoleNotFound(String reason) {

        this.reason = reason;
    }
}