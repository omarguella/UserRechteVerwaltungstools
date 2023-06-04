package User.Recht.Tool.dtos.tokenDtos;

import javax.validation.constraints.NotNull;

public class TokenDto {

    @NotNull
    private String refreshToken;
    @NotNull
    private String accessToken;

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public TokenDto(String refreshToken, String accessToken) {
        this.refreshToken = refreshToken;
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "tokenDto{" +
                "refreshToken='" + refreshToken + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
