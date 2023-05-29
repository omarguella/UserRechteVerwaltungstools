package User.Recht.Tool.exception.Permission;

public class UpdatePermissionDuplicateElement extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public UpdatePermissionDuplicateElement(String reason) {

        this.reason = reason;
    }
}