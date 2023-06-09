package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.permissionDtos.PermissionDto;
import User.Recht.Tool.entity.Permission;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.CannotDeleteInitPermissions;
import User.Recht.Tool.exception.Permission.PermissionNotFound;
import User.Recht.Tool.exception.Permission.PermissionToRoleNotFound;
import User.Recht.Tool.exception.Permission.UserNotAuthorized;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.AuthorizationService;
import User.Recht.Tool.service.PermissionService;
import User.Recht.Tool.service.UserService;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/permissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PermissionResource {

    @Inject
    PermissionService permissionService;
    @Inject
    UserService userService;
    @Inject
    AuthorizationService autorisationService;

    @POST
    @RolesAllowed({"USER"})
    public Response addPermissions(@RequestBody PermissionDto permissionDto,
                                   @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("PERMISSION_MANAGER_POST", token);

            List<Permission> permissions = permissionService.createPermission(permissionDto);
            return Response.ok(permissions).header("status", "NEW PERMISSIONS ARE ADDED")
                    .build();

        }  catch (IllegalAccessException e) {

            return Response.status(406, "THE ACTION SHOULD APPEAR ONCE TIME ONE OF THE FOLLOWING ELEMENTS: GET, POST, PUT, DELETE")
                    .header("status", "THE ACTION SHOULD APPEAR ONCE TIME AND ONE OF THE FOLLOWING ELEMENTS: GET, POST, PUT, DELETE")
                    .build();

        }catch (IllegalArgumentException  e) {

            return Response.status(406, "PERMISSION NAME SHOULD BE ONLY CHARACTER AND _")
                    .header("status", "PERMISSION NAME SHOULD BE ONLY CHARACTER")
                    .build();

        } catch (PermissionNotFound e) {
            return Response.status(406, "xxx")
                    .header("status", "xxx")
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
    public Response getAllPermissions(@Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("PERMISSION_MANAGER_GET", token);
            List<Permission> permissions = permissionService.getAllPermissions();

            return Response.ok(permissions).header("STATUS", "LIST OF PERMISSIONS")
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
    @Path("name/{name}")
    public Response getPermissionsByName(@PathParam("name") String name,
                                         @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("PERMISSION_MANAGER_GET", token);

            List<Permission> permissions = permissionService.getPermissionsByName(name);
            return Response.ok(permissions).header("STATUS", "LIST OF PERMISSIONS")
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
    @Path("key/{key}")
    public Response getPermissionByKey(@PathParam("key") String key,
                                       @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("PERMISSION_MANAGER_GET", token);

            Permission permission = permissionService.getPermissionByKey(key);

            return Response.ok(permission).header("PERMISSION_KEY", key)
                    .build();
        } catch (PermissionNotFound e) {

            return Response.status(406, "PERMISSION DONT EXIST")
                    .header("status", "PERMISSION DONT EXIST").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        }
    }

    @DELETE
    @RolesAllowed({ "USER" })
    @Path("key/{key}")
    public Response deletePermission(@PathParam("key") String key, @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("PERMISSION_MANAGER_DELETE", token);

            Permission permissionToDelete = permissionService.deletePermissionByKey(key);

            return Response.ok(permissionToDelete).header("STATUS", "THE PERMISSION_KEY " + key + " IS DELETED")
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        } catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NOT FOUND")
                    .header("status", "PERMISSION NOT FOUND").build();
        } catch (PermissionToRoleNotFound e) {
            return Response.status(406, "PERMISSION TO ROLE NOT SAVE")
                    .header("status", "PERMISSION TO ROLE NOT SAVE").build();
        } catch (IllegalArgumentException e) {
            return Response.status(406, "TYPE SHOULD BE ALL OR ONE")
                    .header("status", "TYPE SHOULD BE ALL OR ONE").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        }catch (CannotDeleteInitPermissions e) {

            return Response.status(406, "CANNOT DELETE AN INITIALE PERMISSION")
                    .header("STATUS", "CANNOT DELETE AN INITIALE PERMISSION").build();

        }
    }
    @DELETE
    @RolesAllowed({ "USER" })
    @Path("name/{name}")
    public Response deletePermissionsByName(@PathParam("name") String name, @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("PERMISSION_MANAGER_DELETE", token);

            List<Permission> permissionToDelete = permissionService.deletePermissionsByName(name);

            return Response.ok(permissionToDelete).header("STATUS", "THE PERMISSIONS WITH THE NAME  " + name + " are DELETED")
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        } catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NAME NOT FOUND")
                    .header("status", "PERMISSION NAME NOT FOUND").build();
        } catch (PermissionToRoleNotFound e) {
            return Response.status(406, "PERMISSION TO ROLE NOT SAVE")
                    .header("status", "PERMISSION TO ROLE NOT SAVE").build();
        } catch (IllegalArgumentException e) {
            return Response.status(406, "TYPE SHOULD BE ALL OR ONE")
                    .header("status", "TYPE SHOULD BE ALL OR ONE").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        }catch (CannotDeleteInitPermissions e) {

            return Response.status(406, "CANNOT DELETE AN INITIALE PERMISSION")
                    .header("STATUS", "CANNOT DELETE AN INITIALE PERMISSION").build();

        }
    }
}