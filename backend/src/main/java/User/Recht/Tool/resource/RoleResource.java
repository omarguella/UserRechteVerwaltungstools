package User.Recht.Tool.resource;


import User.Recht.Tool.dtos.RoleDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.exception.role.RoleNameDuplicateElementException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.service.RoleService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.ValidationException;
import java.util.List;

@Path("/role")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleResource {
    @Inject
    RoleService roleService;


    private static final Logger LOGGER = LoggerFactory.getLogger(RoleResource.class);


    @POST
    @PermitAll
    public Response createRole(@RequestBody RoleDto roleDto, @Context SecurityContext securityContext)
            throws RoleNameDuplicateElementException, RoleNotFoundException {
        try {
            Role role = roleService.createRole(roleDto);

            return Response.ok(roleService.getRoleByName(roleDto.getName())).header(roleDto.getName(), "IS CREATED")
                    .build();

        } catch (RoleNameDuplicateElementException e) {
            return Response.status(406, "ROLE NAME IS ALREADY USED")
                    .header("status", " ROLE NAME IS ALREADY USED ").build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE IS NOT SAFE CREATED")
                    .header("status", " ROLE IS NOT SAFE CREATED ").build();
        }
    }

    @GET
    @PermitAll
    @Path("/name/{roleName}/")
    public Response getRoleWithName (@PathParam("roleName") String roleName, @Context SecurityContext securityContext)
            throws RoleNotFoundException {
        try {

            Role role = roleService.getRoleByName(roleName);

            return Response.ok(role).header("ROLE", role.getName())
                    .build();

        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE  DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        }

    }


}
