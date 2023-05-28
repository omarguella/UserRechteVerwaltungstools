package User.Recht.Tool.dtos.userDtos;


import User.Recht.Tool.entity.Role;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.NotNull;
import java.util.List;

public class UserDto {

    @NotNull
    private long id;
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String username;
    @NotNull
    private String name;
    @NotNull
    private String lastname;
    private String phoneNumber;
    private boolean isVerifiedEmail;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Role> roles;


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
        return phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean getIsVerifiedEmail() {
        return isVerifiedEmail;
    }

    public void setIsVerifiedEmail(boolean isVerifiedEmail) {
        this.isVerifiedEmail = isVerifiedEmail;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isVerifiedEmail=" + isVerifiedEmail +
                ", roles=" + roles +
                '}';
    }
}
