package User.Recht.Tool.dtos.login;

import javax.validation.constraints.NotBlank;

public class AuthenticationDto {

    @NotBlank
    private String emailOrUsername;
    @NotBlank
    private String password;


    public String getEmailOrUsername() {
        return emailOrUsername;
    }

    public void setEmailOrUsername(String emailOrUsername) {
        this.emailOrUsername = emailOrUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public AuthenticationDto(String emailOrUsername, String password) {
        this.emailOrUsername = emailOrUsername;
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthenticationDto{" +
                "emailOrUsername='" + emailOrUsername + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
