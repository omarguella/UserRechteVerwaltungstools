package User.Recht.Tool.repository;

import User.Recht.Tool.entity.Permission;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PermissionRepository implements PanacheRepository<Permission> {

}