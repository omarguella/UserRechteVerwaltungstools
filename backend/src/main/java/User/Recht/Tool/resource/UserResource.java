package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.userDtos.UpdatePasswordDto;
import User.Recht.Tool.dtos.userDtos.UserDto;
import User.Recht.Tool.dtos.userDtos.UserProfileDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.Permission.CannotCreateUserFromLowerLevel;
import User.Recht.Tool.exception.Permission.EmailAlreadyVerified;
import User.Recht.Tool.exception.Permission.PinNotFound;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNameDuplicateElementException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.UserService;
import User.Recht.Tool.service.serviceImpl.AuthenticationServiceImpl;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
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


    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @POST
    @RolesAllowed({ "USER" })
    @Path("registration/{roleName}")
    public Response createPrivateUser(@Context RoutingContext routingContext,
                                      @RequestBody UserDto userDto, @PathParam("roleName") String roleName,
                                      @Context SecurityContext securityContext) {

        try {
            User user = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            User userToCreate = userService.createPrivateUser(userDto, roleName,user);
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
            return Response.status(406, "CANNOT CREATE A USER FROM A HIGHER ROLE LEVEL")
                    .header("STATUS", "CANNOT CREATE A USER FROM A HIGHER ROLE LEVEL").build();
        }
    }


    @GET
    @Path("/id/{userId}/")
    @RolesAllowed({ "USER" })
    public Response getUserWithId(@Context RoutingContext routingContext,
                                  @PathParam("userId") String id, @Context SecurityContext securityContext) {

     //   checkAllowedPermission (routingContext,"PERMISSION_KEY",assignedTo);

        try {
            String token = routingContext.request().getHeader("Authorization");
            User user = userService.getUserById(Long.parseLong(id));

            return Response.ok(user).header("Email", user.getEmail())
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        }

    }

    @GET
    @Path("/email/{userEmail}/")
    @RolesAllowed({ "USER" })
    public Response getUserWithEmail(@PathParam("userEmail") String userEmail, @Context SecurityContext securityContext){
        try {

            User user = userService.getUserByEmail(userEmail);

            return Response.ok(user).header("Email", user.getEmail())
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        }

    }

    @GET
    @RolesAllowed({ "USER" })
    @Path("/username/{username}/")
    public Response getUserWithUsername(@PathParam("username") String username, @Context SecurityContext securityContext) {
        try {

            User user = userService.getUserByUsername(username);

            return Response.ok(user).header("Email", user.getEmail())
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        }

    }

    @GET
    @RolesAllowed({ "USER" })
    public Response getAllUsers(@Context SecurityContext securityContext) {

        List<User> users = userService.getAllUsers();

        return Response.ok(users).header("STATUS", "LIST OF USERS")
                .build();
    }

    @GET
    @RolesAllowed({ "USER" })
    @Path("/role/{roleName}")
    public Response getUsersByRole(@PathParam("roleName") String roleName, @Context SecurityContext securityContext) {

        try {
            List<User> users = userService.getAllUsersByRole(roleName);
            return Response.ok(users).header("STATUS", "LIST OF USERS OF THIS ROLE " + roleName)
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE  DOSENT EXIST")
                    .header("STATUS", "ROLE DOSENT EXIST").build();
        }


    }

    @DELETE
    @RolesAllowed({ "USER" })
    @Path("/id/{userId}/")
    public Response deleteUserWithId(@PathParam("userId") String id, @Context SecurityContext securityContext) {
        try {

            User user = userService.deleteUserById(Long.parseLong(id));

            return Response.ok(user).header("STATUS", "user is deleted")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER NOT EXIST").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("STATUS", "CANNOT MODIFY A SUPERADMIN").build();
        }

    }

    @PUT
    @RolesAllowed({ "USER" })
    @Path("/email/")
    public Response updateEmailUser (@HeaderParam("userId") String id, @HeaderParam("newEmail") String newEmail, @Context SecurityContext securityContext) {
        try {

            User user = userService.updateEmailUser(Long.parseLong(id),newEmail);

            return Response.ok(user).header("STATUS", "email is updated")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EX").build();
        } catch (ValidationException e){
            return Response.status(406, "EMAIL IS NOT VALID")
                    .header("STATUS", "EMAIL IS NOT VALID").build();
        }catch (DuplicateElementException e){
            return Response.status(406, "USER EMAIL IS ALREADY USED")
                    .header("STATUS", "USER EMAIL IS ALREADY USED").build();
        }

    }

    @PUT
    @RolesAllowed({ "USER" })
    @Path("/password/")
    public Response updatePasswordUser (@HeaderParam("userId") String id, @RequestBody UpdatePasswordDto updatePasswordDto, @Context SecurityContext securityContext) {
        try {

            User user = userService.updatePasswordById(Long.parseLong(id),updatePasswordDto);

            return Response.ok(user).header("STATUS", "PASSWORD IS UPDATED")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        } catch (ValidationException e){
            return Response.status(406, "PASSWORD IS NOT VALID")
                    .header("STATUS", "PASSWORD IS NOT VALID").build();
        } catch (IllegalArgumentException e){
            return Response.status(406, "OLD PASSWORD IS WRONG")
                    .header("STATUS", "OLD PASSWORD IS WRONG").build();
        }

    }

    @PUT
    @RolesAllowed({ "USER" })
    @Path("/profile/")
    public Response updateProfileUser (@HeaderParam("userId") String id, @RequestBody UserProfileDto userProfileDto
            , @Context SecurityContext securityContext) {

        try {

            User user = userService.updateProfilById(Long.parseLong(id),userProfileDto);

            return Response.ok(user).header("STATUS", "PROFILE IS UPDATED")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("STATUS", "USER DOSENT EXIST").build();
        } catch (DuplicateElementException e) {
            return Response.status(406, "USERNAME IS ALREADY USED")
                    .header("STATUS", "USERNAME IS ALREADY USED").build();
        } catch (ValidationException e) {
            return Response.status(406, "PHONENUMBER IS NOT VALID")
                    .header("STATUS", "PHONENUMBER IS NOT VALID").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("STATUS", "CANNOT MODIFY A SUPERADMIN").build();
        }

    }
    @POST
    @Path("/mail/sendPin/")
    @RolesAllowed({ "USER" })
    public Response sendVerifyMail(
             @Context SecurityContext securityContext)  {

        try {
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
    public Response verifyEmail(@HeaderParam("pin") String pin,
            @Context SecurityContext securityContext)  {

        try {
            User user=userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            user= userService.emailVerifyByPin(user,pin);
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
