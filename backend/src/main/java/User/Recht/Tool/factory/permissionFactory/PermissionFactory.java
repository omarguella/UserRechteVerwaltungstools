package User.Recht.Tool.factory.permissionFactory;


import User.Recht.Tool.dtos.PermissionDtos.PermissionDto;
import User.Recht.Tool.service.PermissionService;
import User.Recht.Tool.service.serviceImpl.PermissionServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.AbstractCollection;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.stream.Collectors;

@RequestScoped
public class PermissionFactory {

    @Inject
    PermissionService permissionService;
    private static final Logger LOGGER = LoggerFactory.getLogger(PermissionFactory.class);

        public PermissionDto createPermissionKey(PermissionDto permissionDto) throws IllegalAccessException,IllegalArgumentException{

            permissionDto.setName(permissionDto.getName().toUpperCase());
            List<String> uppercaseList = permissionDto.getListOfAction().stream().map(String::toUpperCase).collect(Collectors.toList());
            permissionDto.setListOfAction(uppercaseList);

            if (permissionDto.getName() == null || !permissionService.isValidName(permissionDto.getName())) {
                throw new IllegalArgumentException("PERMISSION NAME SHOULD BE ONLY CHARACTER");
            } else {
                if (permissionDto.getListOfAction().size()==0){
                    throw new IllegalAccessException("List Of Action is null");
                }
                for(String action: permissionDto.getListOfAction()){

                    LOGGER.info(action);

                    if (!(action.equals("DELETE") || action.equals("GET") || action.equals("POST") || action.equals("PUT"))) {
                        throw new IllegalAccessException("The Action should be one of the following elements: GET, POST, PUT, DELETE");
                    }
                    long countAction= permissionDto.getListOfAction().stream().filter(str -> str.equals(action)).count();
                    if(countAction!=1){
                        throw new IllegalAccessException("THE ACTION SHOULD APPEAR ONCE TIME");
                    }
                }
            }

            return permissionDto;
        }

}
