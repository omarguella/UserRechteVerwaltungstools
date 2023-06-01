package User.Recht.Tool.service;

import User.Recht.Tool.entity.RefreshToken;
import User.Recht.Tool.exception.Token.TokenNotFoundException;

public interface RefreshTokenService {

    RefreshToken getRefreshTokenById(long id) throws TokenNotFoundException;

}
