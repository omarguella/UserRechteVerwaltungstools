package audit.logs.exception;

public class DateException extends Throwable {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DateException(String reason) {

        this.reason = reason;
    }
}
