package User.Recht.Tool.entity;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @Column(name = "userId")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "auto_gen")
    private long id;

    @Column(unique = true, length = 50)
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true, length = 50)
    private String userName;

    @NotNull
    private String name;

    @NotNull
    private String lastName;


    private String phoneNumber;
    private boolean isVerifiedEmail = false;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public User() {
    }

    public boolean getIsVerifiedEmail() {
        return isVerifiedEmail;
    }

    public void setIsVerifiedEmail(boolean isVerifiedEmail) {
        this.isVerifiedEmail = isVerifiedEmail;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", verifiedEmail='" + isVerifiedEmail + '\'' +
                '}';
    }
}