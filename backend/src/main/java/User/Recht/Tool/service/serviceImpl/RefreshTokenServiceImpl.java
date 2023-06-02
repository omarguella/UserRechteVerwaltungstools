package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.entity.RefreshToken;
import User.Recht.Tool.exception.Token.TokenNotFoundException;
import User.Recht.Tool.repository.RefreshTokenRepository;
import User.Recht.Tool.service.RefreshTokenService;
import User.Recht.Tool.util.Encoder;
import User.Recht.Tool.entity.User;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.NotFoundException;
import java.util.List;

@RequestScoped
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Inject
    RefreshTokenRepository refreshTokenRepository;

    @Inject
    Encoder encoder;


    @Transactional
    @Override
    public RefreshToken addRefreshToken(User user, String token) throws TokenNotFoundException {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(encoder.passwordCoder(String.valueOf(user.getId())));
        refreshToken.setToken(encoder.passwordCoder(token));
        refreshTokenRepository.persistAndFlush(refreshToken);
        return getRefreshTokenByToken(refreshToken.getToken());
    }



    @Override
    public RefreshToken getRefreshTokenByToken(String refreshToken) throws TokenNotFoundException {
        try {
            return refreshTokenRepository.find("token", refreshToken).firstResult();
        } catch (NotFoundException e) {
            throw new TokenNotFoundException("TOKEN NOT FOUND");
        }
    }

    @Override
    public List<RefreshToken> getRefreshTokenByUserId(long id) throws TokenNotFoundException {

        try {
            String encodeUserId = encoder.passwordCoder(String.valueOf(id));
            return  refreshTokenRepository.findByUserId(encodeUserId);
        } catch (NotFoundException e) {
            throw new TokenNotFoundException("TOKEN NOT FOUND");
        }
    }


}
