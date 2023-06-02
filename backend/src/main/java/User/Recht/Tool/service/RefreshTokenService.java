package User.Recht.Tool.service;

import User.Recht.Tool.entity.RefreshToken;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Token.TokenNotFoundException;

import javax.transaction.Transactional;
import java.util.List;

public interface RefreshTokenService {


    @Transactional
    RefreshToken addRefreshToken(User user, String token) throws TokenNotFoundException;

    RefreshToken getRefreshTokenByToken(String refreshToken) throws TokenNotFoundException;

    List<RefreshToken> getRefreshTokenByUserId(long id) throws TokenNotFoundException;
}
