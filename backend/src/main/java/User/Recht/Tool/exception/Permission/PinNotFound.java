package User.Recht.Tool.exception.Permission;

public class PinNotFound extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public PinNotFound(String reason) {

        this.reason = reason;
    }
}