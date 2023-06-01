package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.login.AuthenticationDto;
import User.Recht.Tool.dtos.token.TokenDto;
import User.Recht.Tool.entity.RefreshToken;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Authentification.WrongPasswordException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.AuthenticationService;
import User.Recht.Tool.service.UserService;
import User.Recht.Tool.util.Encoder;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthenticationServiceImpl implements AuthenticationService {

    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    @Inject
    UserService userService;
    @Inject
    Encoder encoder;
    @Inject
    Encoder encoder;



    @Transactional
    @Override
    public TokenDto login(AuthenticationDto authenticationDto) throws UserNotFoundException, WrongPasswordException {

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

        RefreshToken refreshToken = refreshTokenService.persistRefreshToken(user);

        String accessToken = JwtTokenUtils.generateTokenString(user);
        String encodedPassword = passwordEncoder.encode(JwtTokenUtils.generateRefreshToken(user, refreshToken));

        TokenDTO tokenDTO = new TokenDTO(accessToken, encodedPassword);
        refreshTokenService.saveRefreshToken(user, tokenDTO.getRefreshToken());

        return tokenDTO;

    }

    public static boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
