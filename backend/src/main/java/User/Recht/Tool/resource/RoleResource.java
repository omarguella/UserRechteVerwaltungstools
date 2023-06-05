package User.Recht.Tool.resource;


import User.Recht.Tool.dtos.roleDtos.RoleDto;
import User.Recht.Tool.dtos.roleDtos.UpdateRoleDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.exception.Permission.LevelRoleException;
import User.Recht.Tool.exception.Permission.PermissionNotFound;
import User.Recht.Tool.exception.Permission.PermissionToRoleNotFound;
import User.Recht.Tool.exception.role.RoleMovedToException;
import User.Recht.Tool.exception.role.RoleNameDuplicateElementException;
import User.Recht.Tool.exception.role.RoleNotAssignedToUserException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.RoleService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/roles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleResource {
    @Inject
    RoleService roleService;


    @POST
    @RolesAllowed({"USER"})
    public Response createRole(@RequestBody RoleDto roleDto, @Context SecurityContext securityContext) {
        try {

            Role role = roleService.createRole(roleDto);
            return Response.ok(role).header(roleDto.getName(), "IS CREATED")
                    .build();

        } catch (RoleNameDuplicateElementException e) {
            return Response.status(406, "ROLE NAME IS ALREADY USED")
                    .header("status", " ROLE NAME IS ALREADY USED ").build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE IS NOT SAFE CREATED")
                    .header("status", " ROLE IS NOT SAFE CREATED ").build();
        } catch (LevelRoleException e) {
            return Response.status(406, "LEVEL SHOULD BE BIGGER THAN 0")
                    .header("status", " LEVEL SHOULD BE BIGGER THAN 0 ").build();
        }
    }

    @GET
    @RolesAllowed({"USER"})
    @Path("/name/{roleName}/")
    public Response getRoleWithName(@PathParam("roleName") String roleName, @Context SecurityContext securityContext) {
        try {
            Role role = roleService.getRoleByName(roleName);
            return Response.ok(role).header("ROLE", role.getName())
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE DOSENT EXIST")
                    .header("status", "ROLE DOSENT EXIST").build();
        }
    }


    @GET
    @RolesAllowed({"USER"})
    public Response getAllRoles(@Context SecurityContext securityContext) {
        List<Role> role = roleService.getAllRoles();
        return Response.ok(role).header("STATUS", "LIST OF ROLES")
                .build();
    }



    @DELETE
    @RolesAllowed({"USER"})
    @Path("/name/{roleName}/")
    public Response deleteRole(@PathParam("roleName") String roleName,@HeaderParam("usersMovedTo") String userMovedTo,
                               @Context SecurityContext securityContext) {
        try {
            Role role = roleService.deleteRoleByName(roleName,userMovedTo);

            return Response.ok(role).header("ROLE", "IS DELETED")
                    .build();

        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE DOSENT EXIST")
                    .header("status", "ROLE DOSENT EXIST").build();
        }catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NOT FOUND")
                    .header("status", "PERMISSION NOT FOUND").build();
        } catch (RoleMovedToException e) {

            return Response.status(406, "ROLE TO MOVE NOT FOUND")
                    .header("status", "ROLE TO MOVE NOT FOUND").build();

        }  catch (PermissionToRoleNotFound e) {
            return Response.status(406, "PERMISSION TO ROLE NOT SAVE")
                    .header("status", "PERMISSION TO ROLE NOT SAVE").build();
        } catch (RoleNotAssignedToUserException e) {
            return Response.status(406, "ROLE NOT AVAILIBALE TO ALL USERS")
                    .header("status", "ROLE NOT AVAILIBALE TO ALL USERS").build();
        }
    }

    @PUT
    @RolesAllowed({"USER"})
    @Path("/name/{roleName}/")
    public Response updateRole(@PathParam("roleName") String roleName, @RequestBody UpdateRoleDto updateRoleDto, @Context SecurityContext securityContext) {

        try {

            Role role = roleService.updateRoleByName(roleName, updateRoleDto);

            return Response.ok(role).header("status", "ROLE IS UPDATED")
                    .build();

        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE DOSENT EXIST")
                    .header("status", "ROLE DOSENT EXIST").build();
        } catch (RoleNameDuplicateElementException e) {
            return Response.status(406, "ROLE NAME IS ALREADY USED")
                    .header("status", "ROLE NAME IS ALREADY USED").build();
        } catch (IllegalArgumentException e) {
            return Response.status(406, "CANNOT CHANGE SUPERADMIN NAME")
                    .header("status", "CANNOT CHANGE SUPERADMIN NAME").build();
        }catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        }
    }

    @GET
    @PermitAll
    @Path("/publicRoles/")
    public Response getPublicRoles() {
        List<Role> roles = roleService.getPublicRoles();
        return Response.ok(roles).header("STATUS", "LIST OF PUBLIC ROLES")
                .build();
    }
    @GET
    @PermitAll
    @Path("/privatRoles/")
    public Response getPrivateRoles(@Context SecurityContext securityContext) {
        List<Role> roles = roleService.getPrivatRoles();
        return Response.ok(roles).header("STATUS", "LIST OF PRIVATE ROLES")
                .build();
    }

}