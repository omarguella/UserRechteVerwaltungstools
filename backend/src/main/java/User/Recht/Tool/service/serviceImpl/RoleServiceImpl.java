package User.Recht.Tool.service.serviceImpl;

import User.Recht.Tool.dtos.RoleDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.role.RoleNameDuplicateElementException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.factory.RoleFactory;
import User.Recht.Tool.repository.RoleRepository;
import User.Recht.Tool.service.RoleService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
public class RoleServiceImpl implements RoleService {

    @Inject
    RoleRepository roleRepository;
    @Inject
    RoleFactory roleFactory;

    @Transactional
    @Override
    public Role createRole(RoleDto roleDto) throws RoleNameDuplicateElementException, RoleNotFoundException {
        roleDto.setName(roleDto.getName().toUpperCase());
        try {
            Role roleCheckWithName = getRoleByName(roleDto.getName());
            throw new RoleNameDuplicateElementException("ROLE NAME " + roleDto.getName() + " EXISTED");
        } catch (RoleNotFoundException e) {
        }
        return saveRole(roleDto);

    }

    @Transactional
    @Override
    public Role saveRole(RoleDto roleDto) throws RoleNotFoundException {
        Role role = new Role();
        role = roleFactory.roleFactory(roleDto);
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

}
