package com.example.demo.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;


@Entity
    @Table(name = "users")
    @Inheritance(strategy = InheritanceType.JOINED)
    public class User {

        @Id
        @Column(name = "userid")
        @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "auto_gen")
        private long userid;

        @Column(unique = true, length = 50)
        @Email
        private String email;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true, length = 50)
    private String username;

    @NotNull
    private String name;
    @NotNull
    private String lastname;

    private String phonenumber;
    private boolean isverifiedemail = false;

    private  String pinemail;


    @ManyToMany
        @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
        private List<Role> roles;


        public long getId() {
            return userid;
        }

        public void setId(long userid) {
            this.userid = userid;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastname() {
            return lastname;
        }

        public void setLastname(String lastname) {
            this.lastname = lastname;
        }

        public String getPhoneNumber() {
            return phonenumber;
        }

        public void setPhoneNumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }


        public String getPinEmail() {
            return pinemail;
        }

        public void setPinEmail(String pinemail) {
            this.pinemail = pinemail;
        }

        public User() {
        }

        public boolean getIsVerifiedEmail() {
            return isverifiedemail;
        }

        public void setIsVerifiedEmail(boolean isverifiedemail) {
            this.isverifiedemail = isverifiedemail;
        }


        public List<Role> getRoles() {
            return roles;
        }

        public void setRoles(List<Role> roles) {
            this.roles = roles;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + userid +
                    ", email='" + email + '\'' +
                    ", password='" + password + '\'' +
                    ", username='" + username + '\'' +
                    ", name='" + name + '\'' +
                    ", lastname='" + lastname + '\'' +
                    ", phoneNumber='" + phonenumber + '\'' +
                    ", verifiedEmail='" + isverifiedemail + '\'' +
                    '}';


        }
    }