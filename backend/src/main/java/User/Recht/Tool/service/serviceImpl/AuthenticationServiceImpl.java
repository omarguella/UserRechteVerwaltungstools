package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.login.AuthenticationDto;
import User.Recht.Tool.dtos.token.TokenDto;
import User.Recht.Tool.entity.RefreshToken;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Authentification.WrongPasswordException;
import User.Recht.Tool.exception.Token.TokenNotFoundException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.AuthenticationService;
import User.Recht.Tool.service.JwtTokenService;
import User.Recht.Tool.service.RefreshTokenService;
import User.Recht.Tool.service.UserService;
import User.Recht.Tool.util.Encoder;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.MalformedClaimException;
import org.jose4j.jwt.NumericDate;
import org.jose4j.jwt.consumer.InvalidJwtException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Inject
    UserService userService;
    @Inject
    Encoder encoder;
    @Inject
    RefreshTokenService refreshTokenService;
    @Inject
    JwtTokenService jwtTokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);


    @Transactional
    @Override
    public TokenDto login(AuthenticationDto authenticationDto, String ipAddress, String deviceName)
            throws UserNotFoundException, WrongPasswordException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, TokenNotFoundException {

        String person=authenticationDto.getEmailOrUsername().toUpperCase();;
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


        String accessToken=jwtTokenService.createToken(user, ipAddress, deviceName);
        String refreshToken=jwtTokenService.createToken(user, ipAddress, deviceName);

        RefreshToken saveRefreshToken = refreshTokenService.addRefreshToken(user,refreshToken);

        TokenDto tokenDto = new TokenDto(saveRefreshToken.getToken(),accessToken);

        return tokenDto;

    }

    @Transactional
    @Override
    public TokenDto getNewAccessToken(User user, String refreshToken, String ipAddress, String deviceName)
            throws TokenNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidJwtException, MalformedClaimException {

        RefreshToken savedRefreshTOken = refreshTokenService.getRefreshTokenByToken(refreshToken);
        TokenDto tokenDto= new TokenDto(refreshToken,jwtTokenService.createToken(user,  ipAddress,  deviceName));
        verifySessionTimer(tokenDto);
        return tokenDto;
    }



    public static void verifySessionTimer(TokenDto tokenDto) throws InvalidJwtException, MalformedClaimException {
        JwtClaims jwtClaims = JwtClaims.parse(tokenDto.getAccessToken());
        NumericDate createdAt = jwtClaims.getIssuedAt();
        LOGGER.info(String.valueOf(createdAt));

    }

    @Transactional
    @Override
    public void logout(String refreshToken) throws TokenNotFoundException {

        refreshTokenService.deleteRefreshTokenByToken(refreshToken);
    }
    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
