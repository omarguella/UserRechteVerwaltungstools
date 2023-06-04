package User.Recht.Tool.entity;

import javax.persistence.*;

@Entity
public class RefreshToken {
    @Column(name = "refreshTokenId")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "auto_gen")
    private long id;
    private String userId;
    private String token;
    private Long issuedAt;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Long issuedAt) {
        this.issuedAt = issuedAt;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", token='" + token + '\'' +
                ", issuedAt='" + issuedAt + '\'' +
                '}';
    }
}
