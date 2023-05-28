package User.Recht.Tool.resource;

import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.role.RoleMovedToException;
import User.Recht.Tool.exception.role.RoleNotAssignedToUserException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.RoleToUserService;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/UserRole")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RoleToUserResource {

    @Inject
    RoleToUserService roleToUserService;

    @POST
    @PermitAll
    @Path("/add/")
    public Response addRole(@HeaderParam("userId") Long userId, @HeaderParam("roleName") String roleName, @Context SecurityContext securityContext)
            throws RoleNotFoundException, UserNotFoundException {
        try {
            User user = roleToUserService.addRoleToUser(userId, roleName);
            return Response.ok(user).header("status", "NEW ROLE IS ADDED")
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
    @PermitAll
    @Path("/delete/")
    public Response deleteRoleFromUser(@HeaderParam("userId") Long userId, @HeaderParam("roleName") String roleName, @HeaderParam("userMovedTo") String userMovedTo, @Context SecurityContext securityContext)
            throws RoleNotFoundException, UserNotFoundException, RoleNotAssignedToUserException, RoleMovedToException {
        try {
           User user= roleToUserService.deleteRoleFromUser(userId,roleName,userMovedTo);
           String status;
           if (user.getRoles().size()==1){
               status="CHANGED";
           }else{
               status="REMOVED";
           }
            return Response.ok(user).header("status", "ROLE IS "+ status+" FROM USER")
                    .build();

        }catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        }catch (RoleNotAssignedToUserException e) {
            return Response.status(406, "ROLE NOT AVAILIBALE TO THE USER")
                    .header("status", "ROLE NOT AVAILIBALE TO THE USER").build();
        } catch (RoleMovedToException e) {
            return Response.status(406, "ROLE TO MOVE NOT FOUND")
                    .header("status", "ROLE TO MOVE NOT FOUND").build();
        }catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        }
    }
}