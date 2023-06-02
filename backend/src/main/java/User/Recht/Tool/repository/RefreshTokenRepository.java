package User.Recht.Tool.repository;

import User.Recht.Tool.entity.RefreshToken;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class RefreshTokenRepository implements PanacheRepository<RefreshToken> {

    public List<RefreshToken> findByUserId(String userId) {
        return list("userId", userId);
    }

}
