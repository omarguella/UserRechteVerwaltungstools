package User.Recht.Tool.service;

import User.Recht.Tool.entity.User;
import io.smallrye.jwt.build.JwtClaimsBuilder;

public interface ClaimsOfUser {
    JwtClaimsBuilder createUserClaims(User user, String ipAddress, String deviceName);
}
