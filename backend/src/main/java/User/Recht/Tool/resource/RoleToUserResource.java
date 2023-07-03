package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.userDtos.UpdateRoleForUsersList;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.DeniedRoleLevel;
import User.Recht.Tool.exception.Permission.UserNotAuthorized;
import User.Recht.Tool.exception.role.RoleMovedToException;
import User.Recht.Tool.exception.role.RoleNotAccessibleException;
import User.Recht.Tool.exception.role.RoleNotAssignedToUserException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.AuthorizationService;
import User.Recht.Tool.service.LogsService;
import User.Recht.Tool.service.RoleToUserService;
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

@Path("/userRole")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleToUserResource {

    @Inject
    RoleToUserService roleToUserService;
    @Inject
    UserService userService;

    @Inject
    AuthorizationService autorisationService;
    @Inject
    LogsService logsService;

    @POST
    @RolesAllowed({"USER"})
    public Response addRole(@HeaderParam("userId") Long userId, @HeaderParam("roleName") String roleName
            , @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkRoleToUserAutorisations(connectedUser, userId, "USER_MANAGER_PUT", token, roleName,null);

            User user = roleToUserService.addRoleToUser(userId, roleName);

            // Send Logs
            logsService.saveLogs("ADD_ROLE_TO_USER",token);

            return Response.ok(user).header("status", "THE ROLE " + roleName + " IS ADDED TO THE USERID " + user.getUsername())
                    .build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT ADD A ROLE TO/FROM USER OF A HIGHER  ROLE LEVEL")
                    .header("STATUS", " CANNOT ADD A ROLE TO/FROM USER OF A HIGHER  ROLE LEVEL").build();
        }
    }

    @DELETE
    @RolesAllowed({"USER"})
    public Response deleteRoleFromUser(@HeaderParam("userId") Long userId, @HeaderParam("roleName") String roleName, @HeaderParam("userMovedTo") String userMovedTo,
                                       @Context RoutingContext routingContext, @Context SecurityContext securityContext) {
        try {

            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkRoleToUserAutorisations(connectedUser, userId, "USER_MANAGER_PUT", token, roleName,userMovedTo);

            User user = roleToUserService.deleteRoleFromUser(userId, roleName, userMovedTo);

            // Send Logs
            logsService.saveLogs("DELETE_ROLE_FROM_USER",token);

            String status;
            if (user.getRoles().size() == 1) {
                status = "CHANGED";
            } else {
                status = "REMOVED";
            }
            return Response.ok(user).header("status", "ROLE IS " + status + " FROM USER")
                    .build();

        }catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (RoleNotFoundException e) {

            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();

        } catch (RoleNotAssignedToUserException e) {
             return Response.status(406, "ROLE NOT AVAILIBALE TO THE USERS")
                    .header("status", "ROLE NOT AVAILIBALE TO THE USERS").build();

        } catch (RoleMovedToException e) {

            return Response.status(406, "ROLE TO MOVE NOT FOUND")
                    .header("status", "ROLE TO MOVE NOT FOUND").build();

        } catch (CannotModifySuperAdminException e) {

            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();

        } catch (UserNotAuthorized e) {

            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();

        }
        catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT DELETE A ROLE TO/FROM USER OF A HIGHER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT DELETE A ROLE TO/FROM USER OF A HIGHER OR SAME ROLE LEVEL").build();
        }
    }

    @PUT
    @RolesAllowed({"USER"})
    public Response updateRolesForUsersWithAction(@RequestBody UpdateRoleForUsersList updateRoleForUsersList,
                                                  @Context RoutingContext routingContext, @Context SecurityContext securityContext) {
        try {

            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            String roleName;
            String movedTo;
            if (updateRoleForUsersList.getAction().equals("ADD")) {
                roleName = updateRoleForUsersList.getAddRole();
                movedTo = null;
            } else {
                roleName = updateRoleForUsersList.getDeleteRole();
                movedTo = updateRoleForUsersList.getMovedTo();
            }

            for (Long userId : updateRoleForUsersList.getUsersIdList()) {
                // CHECK PERMISSIONS
                autorisationService.checkRoleToUserAutorisations(connectedUser, userId, "USER_MANAGER_PUT", token, roleName, movedTo);
            }
            roleToUserService.updateRolesForUsersWithAction(connectedUser,token,updateRoleForUsersList);

            // Send Logs
            logsService.saveLogs("UPDATE_ROLE_OF_USER",token);

            return Response.ok().header("status", "LIST OF USERS UPDATED")
                    .build();
        } catch (UserNotFoundException e) {

            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();

        } catch (CannotModifySuperAdminException e) {

            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();

        } catch (RoleMovedToException e) {

            return Response.status(406, "ROLE TO MOVE NOT FOUND")
                    .header("status", "ROLE TO MOVE NOT FOUND").build();

        } catch (RoleNotFoundException e) {

            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();

        } catch (IllegalAccessException e) {

            return Response.status(406, "DELETE ROLE OR MOVED TO NOT FOUND")
                    .header("status", "DELETE ROLE OR MOVED TO NOT FOUND").build();

        } catch (IllegalArgumentException e) {

            return Response.status(406, "ACTION SHOULD BE ADD OR DELETE AND USERSIDLIST SHOULD BE WITH IDS OF THE TYPE LONG")
                    .header("status", "ACTION SHOULD BE ADD OR DELETE AND USERSIDLIST SHOULD BE WITH IDS OF THE TYPE LONG").build();

        } catch (NullPointerException e) {

            return Response.status(406, "ADD ROLE NOT FOUND")
                    .header("status", "ADDR OLE NOT FOUND").build();

        } catch (RoleNotAssignedToUserException e) {

            return Response.status(406, "ROLE NOT AVAILIBALE TO ALL USERS")
                    .header("status", "ROLE NOT AVAILIBALE TO ALL USERS").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT UPDATE ROLES OF A USER OF A HIGHER OR SAME ROLE LEVEL")
                    .header("STATUS", " CANNOT UPDATE ROLES OF A USER OF A HIGHER OR SAME ROLE LEVEL").build();
        } catch (RoleNotAccessibleException e) {
            return Response.status(406, "ROLE NOT AVAILABLE TO THE USER")
                    .header("status", "ROLE NOT AVAILABLE TO THE USER").build();
        }
    }
}