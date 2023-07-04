package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.permissionDtos.ListPermissionKeysDto;
import User.Recht.Tool.dtos.permissionDtos.PermissionRoleDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.*;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.AuthorizationService;
import User.Recht.Tool.service.LogsService;
import User.Recht.Tool.service.PermissionToRoleService;
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

@Path("/permissionRole")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PermissionToRoleResource {

    @Inject
    PermissionToRoleService permissionToRoleService;
    @Inject
    UserService userService;
    @Inject
    AuthorizationService autorisationService;
    @Inject
    LogsService logsService;

    @POST
    @RolesAllowed({"USER"})
    public Response addPermissionToRole(@RequestBody PermissionRoleDto permissionRoleDto,
                                        @Context RoutingContext routingContext, @Context SecurityContext securityContext) {
        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkPermissionToRoleAutorisations(connectedUser, permissionRoleDto.getRoleName(),
                    "ROLE_MANAGER_PUT", token, permissionRoleDto.getPermissionKey()+"_"+permissionRoleDto.getType());

            PermissionRoleDto permissionRole = permissionToRoleService.addPermissionToRole(permissionRoleDto);

            // Send Logs
            logsService.saveLogs("ADD_PERMISSION_TO_ROLE", token);

            return Response.ok(permissionRole).header("status", "THE  PERMISSION " + permissionRole.getPermissionKey() + " IS ADDED TO THE ROLE " + permissionRole.getRoleName())
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

        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT ADD A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT ADD A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL").build();
        } catch (PermissionNotValid e) {
            return Response.status(406, "CANNOT ADD A PERMISSION OF LOWER TYPE")
                    .header("STATUS", " CANNOT ADD A PERMISSION OF LOWER TYPE").build();
        }

    }

    @POST
    @RolesAllowed({"USER"})
    @Path("/list/")
    public Response addPermissionsListToRole(@RequestBody ListPermissionKeysDto listPermissionKeysDto,
                                             @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            for (String permissionKey:listPermissionKeysDto.getPermissionsList()) {
                // CHECK PERMISSIONS
                autorisationService.checkPermissionToRoleAutorisations(connectedUser, listPermissionKeysDto.getRoleName(), "ROLE_MANAGER_PUT", token,permissionKey);
            }
            List<String> addedPermissions = permissionToRoleService.addPermissionsListToRole(listPermissionKeysDto);

            // Send Logs
            logsService.saveLogs("ADD_PERMISSIONS_TO_ROLE", token);

            return Response.ok(addedPermissions).header("status", "THE  LIST OF PERMISSIONS  IS ADDED TO THE ROLE " + listPermissionKeysDto.getRoleName().toUpperCase())
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
            return Response.status(406, "TYPE SHOULD BE ALL OR ME")
                    .header("status", "TYPE SHOULD BE ALL OR ME").build();
        } catch (ArrayIndexOutOfBoundsException e) {
            return Response.status(406, "TYPE PERMISSION_KEY SHOULD BE IN THIS FORM KEY_TYPE")
                    .header("status", "TYPE PERMISSION_KEY SHOULD BE IN THIS FORM KEY_TYPE").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT ADD A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT ADD A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL").build();
        }catch (PermissionNotValid e) {
            return Response.status(406, "CANNOT ADD A PERMISSION OF LOWER TYPE")
                    .header("STATUS", " CANNOT ADD A PERMISSION OF LOWER TYPE").build();
        }
    }

    @GET
    @RolesAllowed({"USER"})
    @Path("{roleName}")
    public Response getPermissionRole(@HeaderParam("permissionKey") String permissionKey, @PathParam("roleName") String roleName
            , @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkPermissionToRoleAutorisations(connectedUser, roleName, "ROLE_MANAGER_PUT", token,null);

            PermissionRoleDto permissionRole = permissionToRoleService.getPermissionByRole(permissionKey, roleName);

            // Send Logs
            logsService.saveLogs("GET_PERMISSION_OF_ROLE", token);

            return Response.ok(permissionRole).header("status", "THE PERMISSION " + permissionKey + " OF THE ROLE " + roleName)
                    .build();
        } catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NOT FOUND")
                    .header("status", "PERMISSION NOT FOUND").build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (PermissionToRoleNotFound e) {
            return Response.status(406, "PERMISSION OF THE ROLE NOT FOUND")
                    .header("status", "PERMISSION OF THE ROLE NOT FOUND").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT GET A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT GET A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL").build();
        } catch (PermissionNotValid e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @RolesAllowed({"USER"})
    @Path("/all/{roleName}")
    public Response getAllPermissionsOfRole(@PathParam("roleName") String roleName
            , @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkPermissionToRoleAutorisations(connectedUser, roleName, "ROLE_MANAGER_PUT", token, null);

            List<PermissionRoleDto> permissionKeys = permissionToRoleService.getAll(roleName);

            // Send Logs
            logsService.saveLogs("GET_ALL_PERMISSIONS_OF_ROLE", token);

            return Response.ok(permissionKeys).header("status", "THE LIST OF THE ALL PERMISSIONS OF THE ROLE " + roleName)
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT GET A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT GET A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (PermissionNotValid e) {
            throw new RuntimeException(e);
        }
    }
    @GET
    @RolesAllowed({"USER"})
    @Path("/allInString/{roleName}")
    public Response getAllPermissionsOfRoleInString(@PathParam("roleName") String roleName
            , @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkPermissionToRoleAutorisations(connectedUser, roleName, "ROLE_MANAGER_PUT", token, null);

            List<String> permissionKeys = permissionToRoleService.getAllPermissionsOfRole(roleName);

            // Send Logs
            logsService.saveLogs("GET_ALL_PERMISSIONS_OF_ROLE", token);

            return Response.ok(permissionKeys).header("status", "THE LIST OF THE ALL PERMISSIONS OF THE ROLE " + roleName)
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT GET A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT GET A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL").build();
        } catch (PermissionNotValid e) {
            throw new RuntimeException(e);
        }
    }

    @PUT
    @RolesAllowed({"USER"})
    public Response updatePermissionRole(@RequestBody PermissionRoleDto permissionRoleDto,
                                         @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkPermissionToRoleAutorisations(connectedUser, permissionRoleDto.getRoleName(), "ROLE_MANAGER_PUT", token,null);

            PermissionRoleDto permissionRole = permissionToRoleService.updatePermissionRole(permissionRoleDto);

            // Send Logs
            logsService.saveLogs("UPDATE_PERMISSION_OF_ROLE", token);

            return Response.ok(permissionRole).header("status", "THE  PERMISSION " + permissionRole.getPermissionKey() + " OF THE ROLE " + permissionRole.getRoleName() + " IS UPDATED")
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NOT FOUND")
                    .header("status", "PERMISSION NOT FOUND").build();
        } catch (PermissionToRoleNotFound e) {
            return Response.status(406, "PERMISSION TO ROLE NOT SAVE")
                    .header("status", "PERMISSION TO ROLE NOT SAVE").build();
        } catch (IllegalArgumentException e) {
            return Response.status(406, "TYPE SHOULD BE ALL OR ME")
                    .header("status", "TYPE SHOULD BE ALL OR ME").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT UPDATE A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT UPDATE A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL").build();
        } catch (PermissionNotValid e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE
    @PermitAll
    @Path("/{roleName}")

    public Response deletePermissionRole(@QueryParam("permissionKey") String permissionKey, @PathParam("roleName") String roleName,
                                         @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkPermissionToRoleAutorisations(connectedUser, roleName, "ROLE_MANAGER_PUT", token,null);

            PermissionRoleDto permissionRoleDto = permissionToRoleService.deletePermissionRole(permissionKey, roleName);

            // Send Logs
            logsService.saveLogs("DELETE_PERMISSION_OF_ROLE", token);

            return Response.ok(permissionRoleDto).header("status", "THE PERMISSION " + permissionKey + " OF THE ROLE " + roleName + " IS DELETED")
                    .build();
        } catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NOT FOUND")
                    .header("status", "PERMISSION NOT FOUND").build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (PermissionToRoleNotFound e) {
            return Response.status(406, "PERMISSION OF THE ROLE NOT FOUND")
                    .header("status", "PERMISSION OF THE ROLE NOT FOUND").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT DELETE A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT DELETE A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL").build();
        } catch (PermissionNotValid e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE
    @RolesAllowed({"USER"})
    @Path("/list/")
    public Response deleteListePermissionsOfRole(@RequestBody ListPermissionKeysDto listPermissionKeysDto
            , @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkPermissionToRoleAutorisations(connectedUser, listPermissionKeysDto.getRoleName(), "ROLE_MANAGER_PUT", token,null);

            List<String> toDeletePermissions = permissionToRoleService.deleteListePermissionsOfRole(listPermissionKeysDto);

            // Send Logs
            logsService.saveLogs("DELETE_PERMISSIONS_OF_ROLE", token);

            return Response.ok(toDeletePermissions).header("status", "THE LIST OF PERMISSIONS OF THE ROLE IS DELETED"
                            + listPermissionKeysDto.getRoleName().toUpperCase())
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
            return Response.status(406, "TYPE SHOULD BE ALL OR ME")
                    .header("status", "TYPE SHOULD BE ALL OR ME").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT DELETE A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT DELETE A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL").build();
        } catch (PermissionNotValid e) {
            throw new RuntimeException(e);
        }
    }

    @DELETE
    @RolesAllowed({"USER"})
    @Path("all/{roleName}")
    public Response deleteALLPermissionsOfRole(@PathParam("roleName") String roleName
            , @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkPermissionToRoleAutorisations(connectedUser, roleName, "ROLE_MANAGER_PUT", token,null);

            List<String> toDeletePermissions = permissionToRoleService.deleteALLPermissionsOfRole(roleName);

            // Send Logs
            logsService.saveLogs("DELETE_ALL_PERMISSIONS_OF_ROLE", token);

            return Response.ok(toDeletePermissions).header("status", "ALL PERMISSIONS OF THE ROLE " + roleName.toUpperCase() + " ARE DELETED")
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
            return Response.status(406, "TYPE SHOULD BE ALL OR ME")
                    .header("status", "TYPE SHOULD BE ALL OR ME").build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT DELETE A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT DELETE A PERMISSION OF ROLE OF A LOWER OR SAME ROLE LEVEL").build();
        } catch (PermissionNotValid e) {
            throw new RuntimeException(e);
        }
    }

}