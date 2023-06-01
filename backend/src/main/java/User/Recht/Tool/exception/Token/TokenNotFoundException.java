package User.Recht.Tool.exception.Token;

public class TokenNotFoundException extends Throwable {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public TokenNotFoundException(String reason) {

        this.reason = reason;
    }
}
