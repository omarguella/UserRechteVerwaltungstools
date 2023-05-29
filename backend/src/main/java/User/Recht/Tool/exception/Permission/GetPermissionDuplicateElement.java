package User.Recht.Tool.exception.Permission;

public class GetPermissionDuplicateElement extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public GetPermissionDuplicateElement(String reason) {

        this.reason = reason;
    }
}