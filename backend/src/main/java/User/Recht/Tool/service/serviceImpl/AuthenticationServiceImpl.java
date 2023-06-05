package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.DeviceInfosDto;
import User.Recht.Tool.dtos.login.AuthenticationDto;
import User.Recht.Tool.dtos.tokenDtos.TokenDto;
import User.Recht.Tool.dtos.userDtos.UserDto;
import User.Recht.Tool.entity.RefreshToken;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Authentification.WrongPasswordException;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.Permission.PublicRoleNotFound;
import User.Recht.Tool.exception.Permission.SessionTimeoutException;
import User.Recht.Tool.exception.Token.TokenNotFoundException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.*;
import User.Recht.Tool.util.Encoder;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.vertx.ext.web.RoutingContext;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.core.HttpHeaders;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Inject
    UserService userService;
    @Inject
    RoleService roleService;
    @Inject
    Encoder encoder;
    @Inject
    RefreshTokenService refreshTokenService;
    @Inject
    JwtTokenService jwtTokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

    @Transactional
    @Override
    public User createPublicUser(UserDto userDto, String roleName)
            throws DuplicateElementException, NullPointerException, ValidationException, RoleNotFoundException, UserNotFoundException, PublicRoleNotFound {

        List<Role> roles = roleService.getPublicRoles();
        Role role = roleService.getRoleByName(roleName);
        if (!roles.contains(role)) {
            throw new PublicRoleNotFound("THE ROLE IS NOT PUBLIC");
        }
        return userService.createUser(userDto,roleName);
    }


        @Transactional
        @Override
        public TokenDto login (AuthenticationDto authenticationDto, DeviceInfosDto deviceInfos)
            throws
        UserNotFoundException, WrongPasswordException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, TokenNotFoundException
        {

            String person = authenticationDto.getEmailOrUsername().toUpperCase();
            ;
            User user;

            if (isValidEmail(authenticationDto.getEmailOrUsername())) {
                user = userService.getUserByEmail(person);
            } else {
                user = userService.getUserByUsername(person);
            }

            Boolean passwordVerify = user.getPassword().equals(encoder.passwordCoder(authenticationDto.getPassword()));

            if (!passwordVerify) {
                throw new WrongPasswordException("PASSWORD IS WRONG");
            }

            String accessToken = jwtTokenService.createToken(user, deviceInfos, true);
            String refreshToken = jwtTokenService.createToken(user, deviceInfos, false);

            RefreshToken saveRefreshToken = refreshTokenService.addRefreshToken(user, refreshToken);

            TokenDto tokenDto = new TokenDto(saveRefreshToken.getToken(), accessToken);

            return tokenDto;

        }

        @Transactional
        @Override
        public TokenDto getNewAccessToken(User user, String refreshToken, DeviceInfosDto deviceInfosDto)
            throws TokenNotFoundException, IOException, NoSuchAlgorithmException,
                InvalidKeySpecException, InvalidJwtException, MalformedClaimException, SessionTimeoutException {

            RefreshToken savedRefreshTOken = refreshTokenService.getRefreshTokenByToken(refreshToken);
            TokenDto tokenDto = new TokenDto(refreshToken, jwtTokenService.createToken(user, deviceInfosDto, true));
            verifySessionTimer(refreshToken,tokenDto);

            return tokenDto;
        }

        // issuedAt in database
        public void verifySessionTimer(String refreshToken,TokenDto tokenDto) throws SessionTimeoutException, TokenNotFoundException
        {

            Long issuedAt = refreshTokenService.getRefreshTokenByToken(refreshToken).getIssuedAt();

            Map<String, Object> map = listClaimUsingJWT(tokenDto.getAccessToken());
            Long maxSessionTimer = (Long) map.get("maxSessionTimer");

            if (maxSessionTimer != 0 && issuedAt + maxSessionTimer <= currentTimeInMins()) {
                throw new SessionTimeoutException("SESSION TIMEOUT");
            }
        }

        /**
         * listClaimUsingJWT IS COPIED FROM
         * https://stackoverflow.com/a/71676546
         **/
        private static Map<String, Object> listClaimUsingJWT (String accessToken){
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

        @Transactional
        @Override
        public void logout(String refreshToken) throws TokenNotFoundException {

            refreshTokenService.deleteRefreshTokenByToken(refreshToken);
        }

        @Transactional
        @Override
        public void logoutAll(long id) throws TokenNotFoundException {
            refreshTokenService.deleteAllRefreshToken(id);
        }

        public static boolean isValidEmail(String email) {
            Pattern pattern = Pattern.compile(EMAIL_REGEX);
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
        private static long currentTimeInMins() {
            long currentTimeMS = System.currentTimeMillis();
            return currentTimeMS / 60000;
        }
        @Override
        public DeviceInfosDto setDeviceInfos(HttpHeaders headers, RoutingContext routingContext, Boolean proxyAddressForwarding){
            String osName = System.getProperty("os.name");
            String osVersion = System.getProperty("os.version");
            String userAgent = headers.getHeaderString("User-Agent");
            String ipAddress;

            if (proxyAddressForwarding) {
                ipAddress = routingContext.request().getHeader("X-Forwarded-For");
            } else {
                ipAddress = routingContext.request().remoteAddress().host();
            }

            return new DeviceInfosDto(osName,osVersion,userAgent,ipAddress);

        }
}
