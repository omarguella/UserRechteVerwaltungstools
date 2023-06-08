package User.Recht.Tool.exception.Permission;

public class DeniedRoleLevel extends Exception {
    private String reason = null;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public DeniedRoleLevel(String reason) {

        this.reason = reason;
    }
}