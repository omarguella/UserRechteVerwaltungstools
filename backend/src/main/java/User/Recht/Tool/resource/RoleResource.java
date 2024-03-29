package User.Recht.Tool.resource;


import User.Recht.Tool.dtos.roleDtos.RoleDto;
import User.Recht.Tool.dtos.roleDtos.UpdateRoleDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.*;
import User.Recht.Tool.exception.role.*;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.AuthorizationService;
import User.Recht.Tool.service.LogsService;
import User.Recht.Tool.service.RoleService;
import User.Recht.Tool.service.UserService;
import io.vertx.ext.web.RoutingContext;
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
    @Inject
    UserService userService;
    @Inject
    AuthorizationService autorisationService;
    @Inject
    LogsService logsService ;


    @POST
    @RolesAllowed({"USER"})
    public Response createRole(@RequestBody RoleDto roleDto
            , @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("ROLE_MANAGER_POST", token);

            Role role = roleService.createRole(roleDto,token);

            // Send Logs
            logsService.saveLogs("CREATE_ROLE",token);

            return Response.ok(role).header(roleDto.getName(), "IS CREATED")
                    .build();

        } catch (RoleNameDuplicateElementException e) {
            return Response.status(406, "ROLE NAME IS ALREADY USED")
                    .header("status", " ROLE NAME IS ALREADY USED ").build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE IS NOT SAFE CREATED")
                    .header("status", " ROLE IS NOT SAFE CREATED ").build();
        } catch (LevelRoleException e) {
            return Response.status(406, "LEVEL SHOULD BE BIGGER THEN THE MINIMUM LEVEL OF THE ROLE")
                    .header("status", " LEVEL SHOULD BE BIGGER THEN THE MINIMUM LEVEL OF THE ROLE ").build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        }
    }

    @GET
    @RolesAllowed({"USER"})
    @Path("/name/{roleName}/")
    public Response getRoleWithName(@PathParam("roleName") String roleName
            , @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {

            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkRoleManagerAutorisations(connectedUser, roleName, "ROLE_MANAGER_GET", token, null);

            Role role = roleService.getRoleByName(roleName);

            // Send Logs
            logsService.saveLogs("GET_ROLE_BY_NAME",token);

            return Response.ok(role).header("ROLE", role.getName())
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE DOSENT EXIST")
                    .header("status", "ROLE DOSENT EXIST").build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT GET A ROLE OF A LOWER ROLE LEVEL")
                    .header("STATUS", " CANNOT GET A ROLE A ROLE OF A LOWER ROLE LEVEL").build();
        }
    }


    @GET
    @RolesAllowed({"USER"})
    public Response getAvailibaleRoles(@Context SecurityContext securityContext, @Context RoutingContext routingContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("ROLE_MANAGER_GET", token);

            List<Role> role = roleService.getAvailibaleRoles(connectedUser);

            // Send Logs
            logsService.saveLogs("GET_AVAILIBALE_ROLES", token);


            return Response.ok(role).header("STATUS", "LIST OF AVAILIBALE ROLES")
                    .build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        }
    }

    @GET
    @RolesAllowed({"USER"})
    @Path("/edit/")
    public Response getAvailibaleRolesToEdit(@Context SecurityContext securityContext, @Context RoutingContext routingContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("ROLE_MANAGER_GET", token);

            List<Role> role = roleService.getAvailibaleRolesToEdit(connectedUser,token);

            // Send Logs
            logsService.saveLogs("GET_AVAILIBALE_ROLES_TO_EDIT", token);


            return Response.ok(role).header("STATUS", "LIST OF AVAILIBALE ROLES TO EDIT")
                    .build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        }
    }

    @DELETE
    @RolesAllowed({"USER"})
    @Path("/name/{roleName}/")
    public Response deleteRole(@PathParam("roleName") String roleName, @QueryParam("movedToRole") String userMovedTo,
                               @Context RoutingContext routingContext, @Context SecurityContext securityContext) {
        try {

            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkRoleManagerAutorisations(connectedUser, roleName, "ROLE_MANAGER_DELETE", token, userMovedTo);

            Role role = roleService.deleteRoleByName(connectedUser,token,roleName, userMovedTo);

            // Send Logs
            logsService.saveLogs("DELETE_ROLE",token);

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

        } catch (PermissionToRoleNotFound e) {

            return Response.status(406, "PERMISSION TO ROLE NOT SAVE")
                    .header("status", "PERMISSION TO ROLE NOT SAVE").build();

        } catch (RoleNotAssignedToUserException e) {
            return Response.status(406, "ROLE NOT AVAILIBALE TO ALL USERS")
                    .header("status", "ROLE NOT AVAILIBALE TO ALL USERS").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT DELETE A ROLE OF A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT DELETE A ROLE  OF A LOWER OR SAME ROLE LEVEL").build();
        } catch (RoleNotAccessibleException e) {
            return Response.status(406, "ROLE NOT AVAILABLE TO THE USER")
                    .header("status", "ROLE NOT AVAILABLE TO THE USER").build();
        }
    }

    @PUT
    @RolesAllowed({"USER"})
    @Path("/name/{id}/")
    public Response updateRole(@PathParam("id") Long id, @RequestBody UpdateRoleDto updateRoleDto,
                               @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {


            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            Role rolName= roleService.getRoleById(id);

            // CHECK PERMISSIONS
            autorisationService.checkRoleManagerAutorisations(connectedUser, rolName.getName(), "ROLE_MANAGER_PUT", token, null);

            Role role = roleService.updateRoleByName(rolName.getName(), updateRoleDto,token);

            // Send Logs
            logsService.saveLogs("UPDATE_ROLE",token);


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
        }catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        }catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT UPDATE A ROLE OF A LOWER  ROLE LEVEL")
                    .header("STATUS", " CANNOT UPDATE A ROLE  OF A LOWER ROLE LEVEL").build();
        } catch (LevelRoleException e) {
            return Response.status(406, "CANNOT UPDATE A LEVEL LOWER THEN THE CURRENT LEVEL OF USER")
                    .header("STATUS", " CANNOT UPDATE A LEVEL LOWER THEN THE CURRENT LEVEL OF USER").build();
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
    public Response getPrivateRoles(@Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {


            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("ROLE_MANAGER_GET", token);

            List<Role> roles = roleService.getPrivatRoles(connectedUser);

            // Send Logs
            logsService.saveLogs("UPDATE_ROLE", token);

            return Response.ok(roles).header("STATUS", "LIST OF PRIVATE ROLES")
                    .build();
        } catch (UserNotFoundException e) {
            throw new RuntimeException(e);
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        }
    }


}