package User.Recht.Tool.service.serviceImpl;


import User.Recht.Tool.dtos.PermissionDtos.PermissionDto;
import User.Recht.Tool.entity.Permission;
import User.Recht.Tool.exception.Permission.*;
import User.Recht.Tool.factory.permissionFactory.PermissionFactory;
import User.Recht.Tool.repository.PermissionRepository;
import User.Recht.Tool.service.PermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequestScoped
public class PermissionServiceImpl implements PermissionService {

    @Inject
    PermissionRepository permissionRepository;
    @Inject
    PermissionFactory permissionFactory;
    @PersistenceContext
    EntityManager entityManager;

    private static final String NAME_REGEX = "^[a-zA-Z]+$";

    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionServiceImpl.class);

    @Transactional
    @Override
    public List<Permission> createPermission(PermissionDto permissionDto) throws IllegalArgumentException, IllegalAccessException{


        permissionDto=permissionFactory.createPermissionKey(permissionDto);
        checkDuplicatePermissions(permissionDto);

        for (String action: permissionDto.getListOfAction()){
            Permission toSavePermission=new Permission();
            toSavePermission.setName(permissionDto.getName());
            String keyPermission=generateKey(permissionDto.getName(),action);
            toSavePermission.setKey(keyPermission);
            savePermission(toSavePermission);
        }
        return getPermissionsByName(permissionDto.getName());

    }

    @Transactional
    public void savePermission(Permission permission)  {
        permissionRepository.persistAndFlush(permission);
    }

    @Override
    public Permission getPermissionByKey(String key) throws PermissionNotFound {

        key=key.toUpperCase();

        Permission permission = permissionRepository.find("key", key.toUpperCase()).firstResult();

        if (permission != null) {
            return permission;
        } else {
            throw new PermissionNotFound("PERMISSION DONT EXIST");
        }

    }
    @Override
    public List<Permission> getPermissionsByName(String name)  {

        name=name.toUpperCase();

        List<Permission> permissions = entityManager
                .createQuery("SELECT p FROM Permission p WHERE UPPER(p.name) = :name", Permission.class)
                .setParameter("name", name.toUpperCase())
                .getResultList();

            return permissions;

    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.listAll();
    }



    public void checkDuplicatePermissions(PermissionDto permissionDto) {

            if (permissionDto.getListOfAction().contains("DELETE")){
                try {
                    String keyPermission=generateKey(permissionDto.getName(),"DELETE");
                    Permission delete= getPermissionByKey(keyPermission);
                    permissionDto.getListOfAction().remove("DELETE");
                } catch (PermissionNotFound ignored){
                }
            }
        if (permissionDto.getListOfAction().contains("GET")){
            try {
                String keyPermission=generateKey(permissionDto.getName(),"GET");
                Permission get= getPermissionByKey(keyPermission);
                permissionDto.getListOfAction().remove("GET");
            } catch (PermissionNotFound ignored){
            }
        }
        if (permissionDto.getListOfAction().contains("POST")){
            try {
                String keyPermission=generateKey(permissionDto.getName(),"POST");
                Permission post= getPermissionByKey(keyPermission);
                permissionDto.getListOfAction().remove("POST");
            } catch (PermissionNotFound ignored){
            }
        }
        if (permissionDto.getListOfAction().contains("PUT")){
            try {
                String keyPermission=generateKey(permissionDto.getName(),"PUT");
                Permission put= getPermissionByKey(keyPermission);
                permissionDto.getListOfAction().remove("PUT");
            } catch (PermissionNotFound ignored){
            }
        }
    }

    public String generateKey(String name, String key){
        return name+"_"+key;
    }

    @Override
    public boolean isValidName(String name) {
        Pattern pattern = Pattern.compile(NAME_REGEX);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

}
