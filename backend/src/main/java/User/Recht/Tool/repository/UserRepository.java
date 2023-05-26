package User.Recht.Tool.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.dtos.UserDto;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

}