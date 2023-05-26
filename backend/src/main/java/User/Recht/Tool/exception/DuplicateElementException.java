package User.Recht.Tool.exception;

public class DuplicateElementException extends Throwable {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DuplicateElementException(String reason) {

        this.reason = reason;
    }
}
