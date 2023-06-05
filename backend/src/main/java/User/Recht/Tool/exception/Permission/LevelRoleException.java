package User.Recht.Tool.exception.Permission;

public class LevelRoleException extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LevelRoleException(String reason) {

        this.reason = reason;
    }
}