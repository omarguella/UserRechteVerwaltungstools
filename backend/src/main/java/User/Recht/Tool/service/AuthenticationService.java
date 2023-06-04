package User.Recht.Tool.service;

import User.Recht.Tool.dtos.DeviceInfosDto;
import User.Recht.Tool.dtos.login.AuthenticationDto;
import User.Recht.Tool.dtos.tokenDtos.TokenDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Authentification.WrongPasswordException;
import User.Recht.Tool.exception.Permission.SessionTimeoutException;
import User.Recht.Tool.exception.Token.TokenNotFoundException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import io.vertx.ext.web.RoutingContext;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;

import javax.transaction.Transactional;
import javax.ws.rs.core.HttpHeaders;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AuthenticationService {
    @Transactional
    TokenDto login(AuthenticationDto authenticationDto,DeviceInfosDto deviceInfos)
            throws UserNotFoundException, WrongPasswordException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, TokenNotFoundException;

    @Transactional
    TokenDto getNewAccessToken (User user, String refreshtoken,  DeviceInfosDto deviceInfosDto)
            throws TokenNotFoundException, IOException, NoSuchAlgorithmException,SessionTimeoutException,
            InvalidKeySpecException, InvalidJwtException, MalformedClaimException, SessionTimeoutException;

    @Transactional
    void logout(String refreshToken) throws TokenNotFoundException;

    @Transactional
    void logoutAll(long id) throws TokenNotFoundException;

    DeviceInfosDto setDeviceInfos(HttpHeaders headers, RoutingContext routingContext, Boolean proxyAddressForwarding);
}
