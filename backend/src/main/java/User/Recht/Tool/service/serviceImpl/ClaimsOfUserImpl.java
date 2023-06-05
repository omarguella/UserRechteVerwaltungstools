package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.DeviceInfosDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.service.ClaimsOfUser;
import User.Recht.Tool.service.PermissionToRoleService;
import io.smallrye.jwt.build.Jwt;
import io.smallrye.jwt.build.JwtClaimsBuilder;
import org.eclipse.microprofile.jwt.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestScoped
public class ClaimsOfUserImpl implements ClaimsOfUser {
    @Inject
    JwtTokenServiceImpl jwtTokenService;

    @Inject
    PermissionToRoleService permissionToRoleService;
    private static final String ISSUER = "USER_RECHT_TOOL";
    private static final long TOKEN_EXPIRE_IN = 4300L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimsOfUserImpl.class);

    @Override
    public String  createUserClaims(User user, DeviceInfosDto deviceInfos)
            throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

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


        List<String> allPermissionsOfUser = user.getRoles().stream()
                .flatMap(role -> {
                    try {
                        List<String> permissionsOfRole = permissionToRoleService.getAllPermissionsOfRole(role.getName());
                        return permissionsOfRole.stream();
                    } catch (RoleNotFoundException ignored) {
                        return Stream.empty();
                    }
                })
                .distinct()
                .collect(Collectors.toList());


        String privateKeyLocation = "/privatekey.pem";
        PrivateKey privateKey = jwtTokenService.readPrivateKey(privateKeyLocation);
        long currentTimeInSecs = currentTimeInSecs();

        return Jwt.issuer(ISSUER)
                .upn(user.getEmail())
                .groups("USER")
                .subject(user.getEmail())
                .claim("roleNames",rolesNames)
                .claim("maxSessionTimer",maxSessionTimer)
                .claim("minRoleLevel",minRoleLevel)
                .claim("allPermissionsOfUser",allPermissionsOfUser)
                .claim("osName",deviceInfos.getOsName())
                .claim("osVersion",deviceInfos.getOsVersion())
                .claim("userAgent",deviceInfos.getUserAgent())
                .claim("clientIpAddress",deviceInfos.getClientIpAddress())
                .claim("isMailToVerify",isMailToVerify)
                .claim("isVerifiedEmail",user.getIsVerifiedEmail())
                .claim("issuedAt",currentTimeInSecs)
                .issuedAt(currentTimeInSecs)
                .expiresIn(TOKEN_EXPIRE_IN)
                .sign(privateKey);

    }

    static long currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return  (currentTimeMS / 1000);
    }

}
