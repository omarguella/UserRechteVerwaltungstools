package User.Recht.Tool.dtos;

public class DeviceInfosDto {

    private String osName;
    private String osVersion;
    private String userAgent;
    private String clientIpAddress;

    public DeviceInfosDto(String osName, String osVersion, String userAgent, String clientIpAddress) {
        this.osName = osName;
        this.osVersion = osVersion;
        this.userAgent = userAgent;
        this.clientIpAddress = clientIpAddress;
    }

    public DeviceInfosDto() {
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

    @Override
    public String toString() {
        return "DeviceInfosDto{" +
                "osName='" + osName + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", userAgent='" + userAgent + '\'' +
                ", clientIpAddress='" + clientIpAddress + '\'' +
                '}';
    }
}
