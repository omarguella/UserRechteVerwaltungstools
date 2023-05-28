package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.roleDtos.RoleDto;
import User.Recht.Tool.dtos.roleDtos.UpdateRoleDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.exception.role.RoleNameDuplicateElementException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.factory.roleFactorys.RoleFactory;
import User.Recht.Tool.repository.RoleRepository;
import User.Recht.Tool.service.RoleService;
import User.Recht.Tool.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@RequestScoped
public class RoleServiceImpl implements RoleService {

    @Inject
    RoleRepository roleRepository;
    @Inject
    RoleFactory roleFactory;
    @Inject
    UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Transactional
    @Override
    public Role createRole(RoleDto roleDto) throws RoleNameDuplicateElementException, RoleNotFoundException {
        roleDto.setName(roleDto.getName().toUpperCase());
        try {
            Role roleCheckWithName = getRoleByName(roleDto.getName());
            throw new RoleNameDuplicateElementException("ROLE NAME " + roleDto.getName() + " EXISTED");
        } catch (RoleNotFoundException ignored) {
        }
        return saveRole(roleDto);

    }

    @Transactional
    @Override
    public Role saveRole(RoleDto roleDto) throws RoleNotFoundException {
        Role role = roleFactory.roleFactory(roleDto);
        roleRepository.persistAndFlush(role);
        return getRoleByName(role.getName());
    }

    @Override
    public Role getRoleByName(String name) throws RoleNotFoundException {
        Role role = roleRepository.find("name", name.toUpperCase()).firstResult();
        if (role != null) {
            return role;
        } else {
            throw new RoleNotFoundException("ROLE DONT EXIST");
        }
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.listAll();
    }

    @Transactional
    @Override
    public Role deleteRoleByName(String name) throws RoleNotFoundException,CannotModifySuperAdminException {

        if (name.toUpperCase().equals("SUPERADMIN")){
            throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
        }

        try {
            Role role = getRoleByName(name);
            roleRepository.delete(role);
            //Delete alle Berechtigungen, die zur Role gehören
            //users, die zur diese Rolle gehören
            return role;
        } catch (RoleNotFoundException e) {
            throw new RoleNotFoundException("ROLE DOSENT EXIST");
        }
    }

    @Transactional
    @Override
    public Role updateRoleByName(String name, UpdateRoleDto updateRoleDto)
            throws RoleNotFoundException, RoleNameDuplicateElementException, IllegalArgumentException,CannotModifySuperAdminException {

        if (name.toUpperCase().equals("SUPERADMIN")){
            throw new CannotModifySuperAdminException("CANNOT MODIFY A SUPERADMIN");
        }

        Role roleToUpdate = getRoleByName(name);

        if (updateRoleDto.getName() != null) {
            try {
                updateRoleDto.setName(updateRoleDto.getName().toUpperCase());
                if (updateRoleDto.getName().equals("SUPERADMIN")) {
                    throw new IllegalArgumentException("CANNOT CHANGE SUPERADMIN NAME");
                }
                Role checkName = getRoleByName(updateRoleDto.getName());
                if (!Objects.equals(checkName.getId(), roleToUpdate.getId())) {
                    throw new RoleNameDuplicateElementException("USERNAME ALREADY USED");
                }
            } catch (RoleNotFoundException ignored) {
            }
        }
        roleToUpdate = roleFactory.updateRoleFactory(roleToUpdate, updateRoleDto);
        LOGGER.info(String.valueOf(roleToUpdate));
        return saveUpdatedRole(roleToUpdate);
    }

    @Transactional
    public Role saveUpdatedRole(Role role) {
        roleRepository.getEntityManager().merge(role);
        return role;
    }



}
