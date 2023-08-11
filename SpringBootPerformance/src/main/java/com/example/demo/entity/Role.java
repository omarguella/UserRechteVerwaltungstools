package com.example.demo.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, length = 50)
    @NotNull
    private String name;
    private Long sessiontimer=0L;
    private boolean ismailtoverify;
    private int level;
    private boolean isprivate;

    public Role(Long id, String name, Long sessiontimer, boolean ismailtoverify, int level, boolean isprivate) {
        this.id = id;
        this.name = name;
        this.sessiontimer = sessiontimer;
        this.ismailtoverify = ismailtoverify;
        this.level = level;
        this.isprivate = isprivate;
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
        return sessiontimer;
    }

    public void setSessionTimer(Long sessiontimer) {
        this.sessiontimer = sessiontimer;
    }

    public boolean getIsMailToVerify() {
        return ismailtoverify;
    }

    public void setIsMailToVerify(boolean ismailtoverify) {
        ismailtoverify = ismailtoverify;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean getIsPrivate() {
        return isprivate;
    }

    public void setIsPrivate(boolean isprivate) {
        this.isprivate = isprivate;
    }


    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sessionTimer=" + sessiontimer +
                ", isMailToVerify=" + ismailtoverify +
                ", level=" + level +
                ", isPrivate=" + isprivate +
                '}';
    }
}
