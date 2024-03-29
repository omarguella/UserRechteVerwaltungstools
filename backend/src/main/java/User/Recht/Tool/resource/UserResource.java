package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.userDtos.*;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.Permission.*;
import User.Recht.Tool.exception.role.RoleMovedToException;
import User.Recht.Tool.exception.role.RoleNotAccessibleException;
import User.Recht.Tool.exception.role.RoleNotAssignedToUserException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNameDuplicateElementException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.LogsService;
import User.Recht.Tool.service.UserService;
import User.Recht.Tool.service.serviceImpl.AuthorizationServiceImpl;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.xml.bind.ValidationException;
import java.util.List;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;
    @Inject
    AuthorizationServiceImpl autorisationService;

    @Inject
    LogsService logsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @POST
    @RolesAllowed({"USER"})
    @Path("signup/{roleName}")
    public Response createPrivateUser(@Context RoutingContext routingContext,
                                      @RequestBody UserDto userDto, @PathParam("roleName") String roleName,
                                      @Context SecurityContext securityContext) {

        try {

            User user = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkExistedUserPermission("USER_MANAGER_POST", token);

            User userToCreate = userService.createPrivateUser(userDto, roleName, user);

            // Send Logs
            logsService.saveLogs("CREATE_PRIVATE_USER",token);

            return Response.ok(userToCreate).header("Email", userDto.getEmail())
                    .build();

        } catch (UserNameDuplicateElementException e) {
            return Response.status(406, "USERNAME IS ALREADY USED")
                    .header("STATUS", " USERNAME IS ALREADY USED ").build();
        } catch (DuplicateElementException e) {
            return Response.status(406, "EMAIL IS ALREADY USED")
                    .header("STATUS", " EMAIL IS ALREADY USED").build();
        } catch (ValidationException a) {
            return Response.status(406, "EMAIL, PASSWORD OR PHONENUMBER IS NOT VALID")
                    .header("STATUS", "EMAIL, PASSWORD OR PHONENUMBER IS NOT VALID").build();

        } catch (RoleNotFoundException a) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("STATUS", "ROLE NOT FOUND").build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER NOT SAVED")

                    .header("STATUS", "USER NOT SAVED").build();
        } catch (CannotCreateUserFromLowerLevel e) {
            return Response.status(406, "CANNOT CREATE A USER FROM A LOWER ROLE LEVEL")
                    .header("STATUS", "CANNOT CREATE A USER FROM A LOWER ROLE LEVEL").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        }
    }

    @GET
    @Path("/id/{userId}/")
    @RolesAllowed({ "USER" })
    public Response getUserWithId(@Context RoutingContext routingContext,
                                  @PathParam("userId") Long id, @Context SecurityContext securityContext) {


        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkUserManagerAutorisations(connectedUser, id, "USER_MANAGER_GET", token);

            User user = userService.getUserById(id);

            // Send Logs
            logsService.saveLogs("GET_USER_BY_ID",token);


            return Response.ok(user).header("Email", user.getEmail())
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT GET A USER FROM A LOWER ROLE LEVEL")
                    .header("STATUS", "CANNOT GET A USER FROM A LOWER ROLE LEVEL").build();
        }

    }

    @GET
    @Path("/email/{userEmail}/")
    @RolesAllowed({"USER"})
    public Response getUserWithEmail(@PathParam("userEmail") String userEmail,
                                     @Context SecurityContext securityContext, @Context RoutingContext routingContext) {
        try {

            User user = userService.getUserByEmail(userEmail);
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkUserManagerAutorisations(connectedUser, user.getId(), "USER_MANAGER_GET", token);

            // Send Logs
            logsService.saveLogs("GET_USER_BY_EMAIL",token);

            return Response.ok(user).header("Email", user.getEmail())
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT GET A USER FROM A LOWER ROLE LEVEL")
                    .header("STATUS", "CANNOT GET A USER FROM A LOWER ROLE LEVEL").build();
        }

    }

    @GET
    @RolesAllowed({"USER"})
    @Path("/username/{username}/")
    public Response getUserWithUsername(@PathParam("username") String username,
                                        @Context SecurityContext securityContext, @Context RoutingContext routingContext) {
        try {

            User user = userService.getUserByUsername(username);

            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkUserManagerAutorisations(connectedUser, user.getId(), "USER_MANAGER_GET", token);

            // Send Logs
            logsService.saveLogs("GET_USER_BY_USERNAME",token);


            return Response.ok(user).header("Email", user.getEmail())
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT GET A USER FROM A LOWER ROLE LEVEL")
                    .header("STATUS", "CANNOT GET A USER FROM A LOWER ROLE LEVEL").build();
        }

    }

    @GET
    @RolesAllowed({"USER"})
    public Response getAllUsers(@Context SecurityContext securityContext, @Context RoutingContext routingContext) {


        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            autorisationService.checkExistedUserPermission("USER_MANAGER_GET", token);
            List<User> users = userService.getAllUsers(connectedUser,token);
            // Send Logs
            logsService.saveLogs("GET_ALL_USERS",token);

            return Response.ok(users).header("STATUS", "LIST OF USERS")
                    .build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (RoleNotFoundException | RoleNotAccessibleException e) {
            throw new RuntimeException(e);
        }
    }

    @GET
    @RolesAllowed({"USER"})
    @Path("/role/{roleName}")
    public Response getUsersByRole(@PathParam("roleName") String roleName
            , @Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            autorisationService.checkExistedUserPermission("USER_MANAGER_GET", token);

            List<User> users = userService.getAllUsersByRole(connectedUser,token,roleName);

            // Send Logs
            logsService.saveLogs("GET_USERS_BY_ROLE",token);

            return Response.ok(users).header("STATUS", "LIST OF USERS OF THIS ROLE " + roleName)
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE  DOSENT EXIST")
                    .header("STATUS", "ROLE DOSENT EXIST").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (RoleNotAccessibleException e) {
            return Response.status(406, "ROLE NOT AVAILABLE TO THE USER")
                    .header("status", "ROLE NOT AVAILABLE TO THE USER").build();
        }


    }

    @DELETE
    @RolesAllowed({"USER"})
    @Path("/id/{userId}/")
    public Response deleteUserWithId(@PathParam("userId") Long id, @Context RoutingContext routingContext
            , @Context SecurityContext securityContext) {
        try {

            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkUserManagerAutorisations(connectedUser, id, "USER_MANAGER_DELETE", token);

            User user = userService.deleteUserById(id);

            // Send Logs
            logsService.saveLogs("DELETE_USER_BY_ID",token);

            return Response.ok(user).header("STATUS", "user is deleted")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER NOT EXIST").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("STATUS", "CANNOT MODIFY A SUPERADMIN").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT DELETE A USER FROM A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", "CANNOT DELETE A USER FROM A LOWER OR SAME ROLE LEVEL").build();
        }

    }

    @PUT
    @RolesAllowed({"USER"})
    @Path("/email/{userId}")
    public Response updateEmailUser(@PathParam("userId") Long id, @RequestBody EmailDto email,
                                    @Context RoutingContext routingContext, @Context SecurityContext securityContext) {
        try {

            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkUserManagerAutorisations(connectedUser, id, "USER_MANAGER_PUT", token);

            User user = userService.updateEmailUser(id, email.getEmail());

            // Send Logs
            logsService.saveLogs("UPDATE_EMAIL_USER",token);

            return Response.ok(user).header("STATUS", "email is updated")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EX").build();
        } catch (ValidationException e) {
            return Response.status(406, "EMAIL IS NOT VALID")
                    .header("STATUS", "EMAIL IS NOT VALID").build();
        } catch (DuplicateElementException e) {
            return Response.status(406, "USER EMAIL IS ALREADY USED")
                    .header("STATUS", "USER EMAIL IS ALREADY USED").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT UPDATE A USER FROM A LOWER ROLE LEVEL")
                    .header("STATUS", "CANNOT UPDATE A USER FROM A LOWER ROLE LEVEL").build();
        }

    }

    @PUT
    @RolesAllowed({"USER"})
    @Path("/password/{userId}")
    public Response updatePasswordUser(@PathParam("userId") Long id, @Context RoutingContext routingContext,
                                       @RequestBody UpdatePasswordDto updatePasswordDto, @Context SecurityContext securityContext) {
        try {

            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // CHECK PERMISSIONS
            autorisationService.checkUserManagerAutorisations(connectedUser, id, "USER_MANAGER_PUT", token);

            User user = userService.updatePasswordById(connectedUser,id, updatePasswordDto);
            // Send Logs
            logsService.saveLogs("UPDATE_PASSWORD_USER",token);

            return Response.ok(user).header("STATUS", "PASSWORD IS UPDATED")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        } catch (ValidationException e) {
            return Response.status(406, "PASSWORD IS NOT VALID")
                    .header("STATUS", "PASSWORD IS NOT VALID").build();
        } catch (IllegalArgumentException e) {
            return Response.status(406, "OLD PASSWORD IS WRONG")
                    .header("STATUS", "OLD PASSWORD IS WRONG").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel e) {
            return Response.status(406, "CANNOT UPDATE A USER FROM A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", "CANNOT UPDATE A USER FROM A LOWER A LOWER OR SAME ROLE LEVEL").build();
        }

    }

    @PUT
    @RolesAllowed({"USER"})
    @Path("/profile/{userId}")
    public Response updateProfileUser(@PathParam("userId") Long id, @RequestBody UserProfileDto userProfileDto,
                                      @Context RoutingContext routingContext
            , @Context SecurityContext securityContext) {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            autorisationService.checkUserManagerAutorisations(connectedUser, id, "USER_MANAGER_PUT", token);

            User user = userService.updateProfilById(id, userProfileDto);

            // Send Logs
            logsService.saveLogs("UPDATE_PROFIL_USER",token);

            return Response.ok(user).header("STATUS", "PROFILE IS UPDATED")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        } catch (DuplicateElementException e) {
            return Response.status(406, "USERNAME OR EMAIL IS ALREADY USED")
                    .header("STATUS", "USERNAME OR EMAIL IS ALREADY USED").build();
        } catch (ValidationException e) {
            return Response.status(406, "PHONENUMBER IS NOT VALID")
                    .header("STATUS", "PHONENUMBER IS NOT VALID").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("STATUS", "CANNOT MODIFY A SUPERADMIN").build();
        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DeniedRoleLevel | RoleNotFoundException | RoleMovedToException | RoleNotAssignedToUserException e) {
            return Response.status(406, "CANNOT UPDATE A USER FROM A LOWER OR SAME ROLE LEVEL")
                    .header("STATUS", "CANNOT UPDATE A USER FROM A LOWER OR SAME ROLE LEVEL").build();
        }

    }
    @POST
    @Path("/mail/sendPin/")
    @RolesAllowed({ "USER" })
    public Response sendVerifyMail( @Context RoutingContext routingContext,
             @Context SecurityContext securityContext)  {

        try {
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // Send Logs
            logsService.saveLogs("SEND_VERIFY_EMAIL_PIN",token);

            User user=userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            userService.sendPinForEmailVerify(user);
            return Response.accepted().header("STATUS", "EMAIL IS SEND").build();
        }catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        }
    }

    @POST
    @Path("/mail/verify/")
    @RolesAllowed({ "USER" })
    public Response verifyEmail(@Context RoutingContext routingContext,@RequestBody PinVerifyDto pin,
            @Context SecurityContext securityContext)  {

        try {
            User user=userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            user= userService.emailVerifyByPin(user,pin);

            String token = routingContext.request().getHeader("Authorization").substring(7);
            // Send Logs
            logsService.saveLogs("VERIFY_EMAIL",token);


            return Response.accepted(user).header("STATUS", "EMAIL IS VERIFIED").build();
        } catch (PinNotFound e) {
            return Response.status(406, "PIN IS WRONG")
                    .header("STATUS", "PIN IS WRONG").build();
        }catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        } catch (EmailAlreadyVerified e) {
            return Response.status(406, "EMAIL IS ALREADY VERIFIED")
                    .header("STATUS", "EMAIL IS ALREADY VERIFIED").build();        }
    }

}
