package User.Recht.Tool.entity;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.annotation.JsonbTypeAdapter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Column(unique = true, length = 50)
    private String name;
    private Long sessionTimer=0L;
    private boolean isMailToVerify;
    private boolean towFactorAuth;
    private boolean isPrivate;

    public Role(Long id, String name, Long sessionTimer, boolean isMailToVerify, boolean towFactorAuth, boolean isPrivate) {
        this.id = id;
        this.name = name;
        this.sessionTimer = sessionTimer;
        this.isMailToVerify = isMailToVerify;
        this.towFactorAuth = towFactorAuth;
        this.isPrivate = isPrivate;
    }

    public Role() {
    }

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

    public void setIsMailToVerify(boolean mailToVerify) {
        isMailToVerify = mailToVerify;
    }

    public boolean getIsTowFactorAuth() {
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
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sessionTimer=" + sessionTimer +
                ", isMailToVerify=" + isMailToVerify +
                ", towFactorAuth=" + towFactorAuth +
                ", isPrivate=" + isPrivate +
                '}';
    }
}
