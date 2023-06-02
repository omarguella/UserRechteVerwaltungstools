package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.service.ClaimsOfUser;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.eclipse.microprofile.jwt.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import java.util.List;

@RequestScoped
public class ClaimsOfUserImpl implements ClaimsOfUser {
    private static final String ISSUER = "USER_RECHT_TOOL";
    private static final long TOKEN_EXPIRE_IN = 4L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimsOfUserImpl.class);

    @Override
    public JwtClaimsBuilder createUserClaims(User user, String ipAddress, String deviceName){
        JwtClaimsBuilder claims = Jwt.claims();

        // Role Name
        List<String> rolesNames=user.getRoles().stream().map(Role::getName).toList();

        // Maximum Session Timer
        Long maxSessionTimer = user.getRoles()
                .stream()
                .map(Role::getSessionTimer)
                .reduce(Long::max)
                .orElse(0L);

        // Email should be Verified
        boolean isMailToVerify = user.getRoles()
                .stream()
                .anyMatch(Role::getIsMailToVerify);

        // Minimum Level of Roles
        int minRoleLevel = user.getRoles()
                .stream()
                .mapToInt(Role::getLevel)
                .min()
                .orElse(1);

        claims.subject(user.getEmail());
        claims.claim("roleNames",rolesNames);
        claims.claim("maxSessionTimer",maxSessionTimer);
        if(isMailToVerify) {
            claims.claim("verifiedMail", user.getIsVerifiedEmail());
        }
        claims.claim("minRoleLevel",minRoleLevel);
        claims.claim("ipAddress",ipAddress);
        claims.claim("deviceName",deviceName);

        claims.issuer(ISSUER);
        long currentTimeInSecs = currentTimeInSecs();
        LOGGER.info(String.valueOf(currentTimeInSecs));
        claims.issuedAt(currentTimeInSecs);
        claims.expiresIn(TOKEN_EXPIRE_IN);
        claims.groups("USER");

        return claims;
    }

    private static long currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return  (currentTimeMS / 1000);
    }

}
