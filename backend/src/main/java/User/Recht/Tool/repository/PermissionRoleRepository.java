package User.Recht.Tool.repository;

import User.Recht.Tool.entity.PermissionRole;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class PermissionRoleRepository implements PanacheRepository<PermissionRole> {
    public PermissionRole findByPermissionIdAndRoleId(Long permissionId, Long roleId) {
        return find("permission.id = ?1 and role.id = ?2", permissionId, roleId).firstResult();
    }

    public List<PermissionRole> findByRoleId(Long roleId) {
        return find("role.id", roleId).list();
    }

}