package User.Recht.Tool.exception.Permission;

public class DeletePermissionDuplicateElement extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DeletePermissionDuplicateElement(String reason) {

        this.reason = reason;
    }
}