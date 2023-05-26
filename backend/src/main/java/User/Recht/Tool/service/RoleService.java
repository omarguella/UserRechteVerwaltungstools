package User.Recht.Tool.service;

import User.Recht.Tool.dtos.RoleDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.role.RoleNameDuplicateElementException;
import User.Recht.Tool.exception.role.RoleNotFoundException;

import javax.transaction.Transactional;

public interface RoleService {
    @Transactional
    Role createRole(RoleDto roleDto) throws RoleNameDuplicateElementException, RoleNotFoundException;

    @Transactional
    Role saveRole(RoleDto roleDto) throws RoleNotFoundException;

    Role getRoleByName(String name) throws RoleNotFoundException;
}
