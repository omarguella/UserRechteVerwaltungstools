package User.Recht.Tool.dtos.auditLogDto;


import javax.json.bind.annotation.JsonbProperty;

public class LogDto {

        private long id;
        @JsonbProperty("userId")
        private String userId;

        @JsonbProperty("userEmail")
        private String userEmail;

        @JsonbProperty("action")
        private String action;

        @JsonbProperty("osName")
        private String osName;

        @JsonbProperty("osVersion")
        private String osVersion;

        @JsonbProperty("userAgent")
        private String userAgent;

        @JsonbProperty("clientIpAddress")
        private String clientIpAddress;

        @JsonbProperty("isVerifiedEmail")
        private boolean isVerifiedEmail;

        @JsonbProperty("date")
        private String date;
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

        public String getDate() {
                return date;
        }

        public void setDate(String date) {
                this.date = date;
        }

        public LogDto(long id, String userId, String userEmail, String action, String osName, String osVersion, String userAgent, String clientIpAddress, boolean isVerifiedEmail, String date) {
                this.id = id;
                this.userId = userId;
                this.userEmail = userEmail;
                this.action = action;
                this.osName = osName;
                this.osVersion = osVersion;
                this.userAgent = userAgent;
                this.clientIpAddress = clientIpAddress;
                this.isVerifiedEmail = isVerifiedEmail;
                this.date = date;
        }

        public LogDto() {
        }

        @Override
        public String toString() {
                return "LogDto{" +
                        "id=" + id +
                        ", userId='" + userId + '\'' +
                        ", userEmail='" + userEmail + '\'' +
                        ", action='" + action + '\'' +
                        ", osName='" + osName + '\'' +
                        ", osVersion='" + osVersion + '\'' +
                        ", userAgent='" + userAgent + '\'' +
                        ", clientIpAddress='" + clientIpAddress + '\'' +
                        ", isVerifiedEmail=" + isVerifiedEmail +
                        ", date='" + date + '\'' +
                        '}';
        }
}
