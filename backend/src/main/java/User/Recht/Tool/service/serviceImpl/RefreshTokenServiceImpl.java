package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.entity.RefreshToken;
import User.Recht.Tool.exception.Token.TokenNotFoundException;
import User.Recht.Tool.repository.RefreshTokenRepository;
import User.Recht.Tool.service.RefreshTokenService;
import User.Recht.Tool.util.Encoder;
import User.Recht.Tool.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleToUserServiceImpl.class);


    @Transactional
    @Override
    public RefreshToken addRefreshToken(User user, String token) throws TokenNotFoundException {

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(encoder.passwordCoder(String.valueOf(user.getId())));
        refreshToken.setToken(encoder.passwordCoder(token));
        refreshToken.setIssuedAt(currentTimeInMins());
        refreshTokenRepository.persistAndFlush(refreshToken);
        return getRefreshTokenByToken(refreshToken.getToken());
    }



    @Override
    public RefreshToken getRefreshTokenByToken(String refreshToken) throws TokenNotFoundException {

            RefreshToken savedRefreshToken= refreshTokenRepository.find("token", refreshToken).firstResult();
            if(savedRefreshToken==null) {
                throw new TokenNotFoundException("TOKEN NOT FOUND");
            }
            return savedRefreshToken;
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

    @Transactional
    @Override
    public void deleteRefreshTokenByToken(String token) throws TokenNotFoundException {
        RefreshToken refreshToken = getRefreshTokenByToken(token);
        refreshTokenRepository.delete(refreshToken);
    }

    @Override
    public void deleteAllRefreshToken(long id) throws TokenNotFoundException {
        List<RefreshToken> refreshTokenByUserId = getRefreshTokenByUserId(id);
        for (RefreshToken refreshToken : refreshTokenByUserId) {
            refreshTokenRepository.delete(refreshToken);
        }
    }
    private static long currentTimeInMins() {
        long currentTimeMS = System.currentTimeMillis();
        return currentTimeMS / 60000;
    }

    }
