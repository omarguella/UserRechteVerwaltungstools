package User.Recht.Tool.entity;

import org.hibernate.Session;

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

    public boolean isTowFactorAuth() {
        return towFactorAuth;
    }

    public void setTowFactorAuth(boolean towFactorAuth) {
        this.towFactorAuth = towFactorAuth;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sessionTimer=" + sessionTimer +
                ", isMailToVerify=" + isMailToVerify +
                ", towFactorAuth=" + towFactorAuth +
                '}';
    }
}
