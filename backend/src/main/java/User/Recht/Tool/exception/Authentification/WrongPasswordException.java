package User.Recht.Tool.exception.Authentification;

public class WrongPasswordException extends Throwable {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public WrongPasswordException(String reason) {

        this.reason = reason;
    }
}
