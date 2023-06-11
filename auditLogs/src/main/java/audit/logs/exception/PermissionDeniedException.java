package audit.logs.exception;

public class PermissionDeniedException extends Throwable {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PermissionDeniedException(String reason) {

        this.reason = reason;
    }
}
