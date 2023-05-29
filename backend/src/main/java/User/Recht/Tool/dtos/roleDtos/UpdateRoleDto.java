package User.Recht.Tool.dtos.roleDtos;

import javax.validation.constraints.NotNull;

public class UpdateRoleDto {

    @NotNull
    private String name;
    private Long sessionTimer;
    private boolean isMailToVerify;
    private boolean towFactorAuth;
    private boolean isPrivate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSessionTimer() {
        return sessionTimer;
    }

    public void setSessionTimer(Long sessionTimer) {
        this.sessionTimer = sessionTimer;
    }

    public boolean getIsMailToVerify() {
        return isMailToVerify;
    }

    public void setIsMailToVerify(boolean mailToVerify) {
        isMailToVerify = mailToVerify;
    }

    public boolean getTowFactorAuth() {
        return towFactorAuth;
    }

    public void setTowFactorAuth(boolean towFactorAuth) {
        this.towFactorAuth = towFactorAuth;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }


    @Override
    public String toString() {
        return "UpdateRoleDto{" +
                "name='" + name + '\'' +
                ", sessionTimer=" + sessionTimer +
                ", isMailToVerify=" + isMailToVerify +
                ", towFactorAuth=" + towFactorAuth +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
