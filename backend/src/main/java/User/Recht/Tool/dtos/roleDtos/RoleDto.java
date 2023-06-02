package User.Recht.Tool.dtos.roleDtos;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

public class RoleDto {

    private Long id;
    @NotNull
    private String name;
    private Long sessionTimer;
    private boolean isMailToVerify;
    private int level;
    private boolean isPrivate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public void setIsMailToVerify(boolean isMailToVerify) {
        isMailToVerify = isMailToVerify;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    @Override
    public String toString() {
        return "RoleDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sessionTimer=" + sessionTimer +
                ", mailToVerify=" + isMailToVerify+
                ", level=" + level +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
