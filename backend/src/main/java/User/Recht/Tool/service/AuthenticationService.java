package User.Recht.Tool.service;

import User.Recht.Tool.dtos.login.AuthenticationDto;
import User.Recht.Tool.dtos.token.TokenDto;
import User.Recht.Tool.exception.Authentification.WrongPasswordException;
import User.Recht.Tool.exception.Token.TokenNotFoundException;
import User.Recht.Tool.exception.user.UserNotFoundException;

import javax.transaction.Transactional;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface AuthenticationService {
    @Transactional
    TokenDto login(AuthenticationDto authenticationDto, String ipAddress, String deviceName)
            throws UserNotFoundException, WrongPasswordException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, TokenNotFoundException;
}
