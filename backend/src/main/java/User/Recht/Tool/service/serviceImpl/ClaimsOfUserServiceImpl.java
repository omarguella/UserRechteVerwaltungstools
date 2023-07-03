package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.DeviceInfosDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.service.ClaimsOfUserService;
import User.Recht.Tool.service.PermissionToRoleService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.smallrye.jwt.build.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequestScoped
public class ClaimsOfUserServiceImpl implements ClaimsOfUserService {
    @Inject
    JwtTokenServiceImpl jwtTokenService;

    @Inject
    PermissionToRoleService permissionToRoleService;
    private static final String ISSUER = "USER_RECHT_TOOL";
    private static final long TOKEN_EXPIRE_IN = 4300L;
    private static final Logger LOGGER = LoggerFactory.getLogger(ClaimsOfUserServiceImpl.class);

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

        toString();
        return Jwt.issuer(ISSUER)
                .upn(user.getEmail())
                .groups("USER")
                .subject(user.getEmail())
                .claim("roleNames",rolesNames)
                .claim("type","USER")
                .claim("userId", String.valueOf(user.getId()))
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


    /**
     * listClaimUsingJWT IS COPIED FROM
     * https://stackoverflow.com/a/71676546
     **/
   @Override
   public Map<String, Object> listClaimUsingJWT(String accessToken){
        Map<String, Object> map = new HashMap<>();

        try {
            SignedJWT signedJWT = SignedJWT.parse(accessToken);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
            Map<String, Object> myClaim = claimsSet.getClaims();

            String[] keySet = myClaim.keySet().toArray(new String[0]);

            for (String s : keySet) {
                map.put(s, myClaim.get(s));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;

    }

    static long currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();
        return  (currentTimeMS / 1000);
    }

}
