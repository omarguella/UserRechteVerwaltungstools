package User.Recht.Tool.repository;

import User.Recht.Tool.entity.RefreshToken;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RefreshTokenRepository implements PanacheRepository<RefreshToken> {

}