package User.Recht.Tool.service;

import User.Recht.Tool.dtos.login.AuthenticationDto;
import User.Recht.Tool.dtos.token.TokenDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Authentification.WrongPasswordException;
import User.Recht.Tool.exception.Permission.SessionTimeoutException;
import User.Recht.Tool.exception.Token.TokenNotFoundException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.consumer.InvalidJwtException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AuthenticationService {
    @Transactional
    TokenDto login(AuthenticationDto authenticationDto, String ipAddress, String deviceName)
            throws UserNotFoundException, WrongPasswordException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, TokenNotFoundException;

    @Transactional
    TokenDto getNewAccessToken (User user, String refreshtoken, String ipAddress, String deviceName)
            throws TokenNotFoundException, IOException, NoSuchAlgorithmException,SessionTimeoutException,
            InvalidKeySpecException, InvalidJwtException, MalformedClaimException, SessionTimeoutException;

    @Transactional
    void logout(String refreshToken) throws TokenNotFoundException;
}
