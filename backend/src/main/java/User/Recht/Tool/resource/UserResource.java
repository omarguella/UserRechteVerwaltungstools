package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.userDtos.UpdatePasswordDto;
import User.Recht.Tool.dtos.userDtos.UserDto;
import User.Recht.Tool.dtos.userDtos.UserProfileDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.exception.user.UserNameDuplicateElementException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.UserService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

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




    @POST
    @PermitAll
    @Path("registration/{roleName}")
    public Response createUser(@RequestBody UserDto userDto, @PathParam("roleName") String roleName){
        try {

            User user = userService.createUser(userDto,roleName);
            return Response.ok(user).header("Email", userDto.getEmail())
                    .build();

        } catch (UserNameDuplicateElementException e) {
            return Response.status(406, "USERNAME IS ALREADY USED")
                    .header("status", " USERNAME IS ALREADY USED ").build();
        } catch (DuplicateElementException e) {
            return Response.status(406, "EMAIL IS ALREADY USED")
                    .header("status", " EMAIL IS ALREADY USED").build();
        } catch (ValidationException a) {
            return Response.status(406, "EMAIL, PASSWORD OR PHONENUMBER IS NOT VALID")
                    .header("status", "EMAIL, PASSWORD OR PHONENUMBER IS NOT VALID").build();

        }catch (RoleNotFoundException a) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER NOT SAVED")
                    .header("status", "USER NOT SAVED").build();
        }
    }

    @GET
    @Path("/id/{userId}/")
    @RolesAllowed({ "USER" })
    public Response getUserWithId(@PathParam("userId") String id, @Context SecurityContext securityContext) {
        try {

            User user = userService.getUserById(Long.parseLong(id));

            return Response.ok(user).header("Email", user.getEmail())
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER  DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
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
                    .header("status", "USER DOSENT EXIST").build();
        }

    }

    @GET
    @PermitAll
    @Path("/username/{username}/")
    public Response getUserWithUsername(@PathParam("username") String username, @Context SecurityContext securityContext) {
        try {

            User user = userService.getUserByUsername(username);

            return Response.ok(user).header("Email", user.getEmail())
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        }

    }

    @GET
    @PermitAll
    public Response getAllUsers(@Context SecurityContext securityContext) {

        List<User> users = userService.getAllUsers();

        return Response.ok(users).header("STATUS", "LIST OF USERS")
                .build();
    }

    @GET
    @PermitAll
    @Path("/role/{roleName}")
    public Response getUsersByRole(@PathParam("roleName") String roleName, @Context SecurityContext securityContext) {

        try {
            List<User> users = userService.getAllUsersByRole(roleName);
            return Response.ok(users).header("STATUS", "LIST OF USERS OF THIS ROLE " + roleName)
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE  DOSENT EXIST")
                    .header("status", "ROLE DOSENT EXIST").build();
        }


    }

    @DELETE
    @PermitAll
    @Path("/id/{userId}/")
    public Response deleteUserWithId(@PathParam("userId") String id, @Context SecurityContext securityContext) {
        try {

            User user = userService.deleteUserById(Long.parseLong(id));

            return Response.ok(user).header("status", "user is deleted")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER NOT EXIST").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        }

    }

    @PUT
    @PermitAll
    @Path("/email/")
    public Response updateEmailUser (@HeaderParam("userId") String id, @HeaderParam("newEmail") String newEmail, @Context SecurityContext securityContext) {
        try {

            User user = userService.updateEmailUser(Long.parseLong(id),newEmail);

            return Response.ok(user).header("status", "email is updated")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EX").build();
        } catch (ValidationException e){
            return Response.status(406, "EMAIL IS NOT VALID")
                    .header("status", "EMAIL IS NOT VALID").build();
        }catch (DuplicateElementException e){
            return Response.status(406, "USER EMAIL IS ALREADY USED")
                    .header("status", "USER EMAIL IS ALREADY USED").build();
        }

    }

    @PUT
    @PermitAll
    @Path("/password/")
    public Response updatePasswordUser (@HeaderParam("userId") String id, @RequestBody UpdatePasswordDto updatePasswordDto, @Context SecurityContext securityContext) {
        try {

            User user = userService.updatePasswordById(Long.parseLong(id),updatePasswordDto);

            return Response.ok(user).header("status", "PASSWORD IS UPDATED")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (ValidationException e){
            return Response.status(406, "PASSWORD IS NOT VALID")
                    .header("status", "PASSWORD IS NOT VALID").build();
        } catch (IllegalArgumentException e){
            return Response.status(406, "OLD PASSWORD IS WRONG")
                    .header("status", "OLD PASSWORD IS WRONG").build();
        }

    }

    @PUT
    @PermitAll
    @Path("/profile/")
    public Response updateProfileUser (@HeaderParam("userId") String id, @RequestBody UserProfileDto userProfileDto, @Context SecurityContext securityContext) {

        try {

            User user = userService.updateProfilById(Long.parseLong(id),userProfileDto);

            return Response.ok(user).header("status", "PROFILE IS UPDATED")
                    .build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER DOSENT EXIST")
                    .header("status", "USER DOSENT EXIST").build();
        } catch (DuplicateElementException e) {
            return Response.status(406, "USERNAME IS ALREADY USED")
                    .header("status", "USERNAME IS ALREADY USED").build();
        } catch (ValidationException e) {
            return Response.status(406, "PHONENUMBER IS NOT VALID")
                    .header("status", "PHONENUMBER IS NOT VALID").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        }

    }


}
