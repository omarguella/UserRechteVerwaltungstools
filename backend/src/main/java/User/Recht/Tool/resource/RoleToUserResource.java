package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.userDtos.UpdateRoleForUsersList;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.role.RoleMovedToException;
import User.Recht.Tool.exception.role.RoleNotAssignedToUserException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.RoleToUserService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.annotation.security.PermitAll;
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

    @POST
    @RolesAllowed({ "USER" })
    public Response addRole(@HeaderParam("userId") Long userId, @HeaderParam("roleName") String roleName
            , @Context SecurityContext securityContext) {
        try {
            User user = roleToUserService.addRoleToUser(userId, roleName);
            return Response.ok(user).header("status", "THE ROLE "+roleName+" IS ADDED TO THE USERID "+user.getUsername())
                    .build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        }catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        }
    }

    @DELETE
    @RolesAllowed({ "USER" })
    public Response deleteRoleFromUser(@HeaderParam("userId") Long userId, @HeaderParam("roleName") String roleName, @HeaderParam("userMovedTo") String userMovedTo, @Context SecurityContext securityContext) {
        try {
            User user = roleToUserService.deleteRoleFromUser(userId, roleName, userMovedTo);
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

        }
    }

    @PUT
    @RolesAllowed({ "USER" })
    public Response updateRolesForUsersWithAction(@RequestBody UpdateRoleForUsersList updateRoleForUsersList, @Context SecurityContext securityContext) {
        try {
            roleToUserService.updateRolesForUsersWithAction(updateRoleForUsersList);
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

            return Response.status(406, "DELETEROLE OR MOVEDTO NOT FOUND")
                    .header("status", "DELETEROLE OR MOVEDTO NOT FOUND").build();

        } catch (IllegalArgumentException e) {

            return Response.status(406, "ACTION SHOULD BE ADD OR DELETE AND USERSIDLIST SHOULD BE WITH IDS OF THE TYPE LONG")
                    .header("status", "ACTION SHOULD BE ADD OR DELETE AND USERSIDLIST SHOULD BE WITH IDS OF THE TYPE LONG").build();

        }
        catch (NullPointerException e) {

            return Response.status(406, "ADDROLE NOT FOUND")
                    .header("status", "ADDROLE NOT FOUND").build();

        } catch (RoleNotAssignedToUserException e) {

            return Response.status(406, "ROLE NOT AVAILIBALE TO ALL USERS")
                    .header("status", "ROLE NOT AVAILIBALE TO ALL USERS").build();
        }
    }
}