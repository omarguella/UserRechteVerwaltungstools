package audit.logs.entity;


import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Logs")
@Inheritance(strategy = InheritanceType.JOINED)
public class Log {

    @Id
    @Column(name = "logId")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "auto_gen")
    private long id;
    private String userId;
    private String userEmail;
    @Column(length = 50)
    @JsonbDateFormat("dd-MM-yyyy")
    private LocalDate date;
    @NotNull
    private String action;
    @NotNull
    private String osName;
    @NotNull
    private String osVersion;
    @NotNull
    private String userAgent;
    @NotNull
    private String clientIpAddress;
    @NotNull
    private boolean isVerifiedEmail;

    public Log( String userId, String userEmail, LocalDate date, String action, String osName,
               String osVersion, String userAgent, String clientIpAddress, boolean isVerifiedEmail) {
        this.userId = userId;
        this.userEmail = userEmail;
        this.date = date;
        this.action = action;
        this.osName = osName;
        this.osVersion = osVersion;
        this.userAgent = userAgent;
        this.clientIpAddress = clientIpAddress;
        this.isVerifiedEmail = isVerifiedEmail;
    }

    public Log() {
    }

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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getClientIpAddress() {
        return clientIpAddress;
    }

    public void setClientIpAddress(String clientIpAddress) {
        this.clientIpAddress = clientIpAddress;
    }

    public boolean getIsVerifiedEmail() {
        return isVerifiedEmail;
    }

    public void setIsVerifiedEmail(boolean isVerifiedEmail) {
        this.isVerifiedEmail = isVerifiedEmail;
    }

    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", userId=" + userId +
                ", userEmail='" + userEmail + '\'' +
                ", date=" + date +
                ", action='" + action + '\'' +
                ", osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", clientIpAddress='" + clientIpAddress + '\'' +
                ", isVerifiedEmail='" + isVerifiedEmail + '\'' +
                '}';
    }
}