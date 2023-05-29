package User.Recht.Tool.exception.Permission;

public class CreatePermissionDuplicateElement extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public CreatePermissionDuplicateElement(String reason) {

        this.reason = reason;
    }
}