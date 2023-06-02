package User.Recht.Tool.resource;


import User.Recht.Tool.dtos.login.AuthenticationDto;
import User.Recht.Tool.dtos.token.TokenDto;
import User.Recht.Tool.dtos.userDtos.UserDto;
import User.Recht.Tool.entity.Role;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Authentification.WrongPasswordException;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.Token.TokenNotFoundException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.user.UserNameDuplicateElementException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.AuthenticationService;
import User.Recht.Tool.service.RoleService;
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

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    @Inject
    AuthenticationService authenticationService;
    @Inject
    UserService userService;
    @Inject
    RoleService roleService;

    @POST
    @PermitAll
    @Path("registration/{roleName}")
    public Response createUser(@RequestBody UserDto userDto, @PathParam("roleName") String roleName) {
        try {

            User user = userService.createUser(userDto, roleName);
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

        } catch (RoleNotFoundException a) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();

        } catch (UserNotFoundException e) {
            return Response.status(406, "USER NOT SAVED")
                    .header("status", "USER NOT SAVED").build();
        }
    }

    @GET
    @PermitAll
    @Path("/publicRole/")
    public Response getPublicRoles(@Context SecurityContext securityContext) {
        List<Role> roles = roleService.getPublicRoles();
        return Response.ok(roles).header("STATUS", "LIST OF PUBLIC ROLES")
                .build();
    }

    @PermitAll
    @POST
    @Path("/login")
    public Response login(@RequestBody AuthenticationDto authenticationDto
            , @Context SecurityContext securityContext) throws Exception {

        try {
            TokenDto tokenDto = authenticationService.login(authenticationDto, "ipadress", "devicename");
            return Response.ok(tokenDto).header("ACCESS_TOKEN", tokenDto.getAccessToken())
                    .header("REFRESH_TOKEN", tokenDto.getRefreshToken())
                    .build();
        } catch (TokenNotFoundException e) {
            return Response.status(406, "TOKEN NOT CREATED")
                    .header("status", "TOKEN NOT CREATED").build();
        } catch (WrongPasswordException e) {
            return Response.status(406, "WRONG PASSWORD")
                    .header("status", "WRONG PASSWORD").build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER NOT FOUND")
                    .header("status", "USER NOT FOUND").build();
        }
    }

    @RolesAllowed({"USER"})
    @GET
    @Path("/getAccessToken/")
    public Response getNewAccessToken(@Context SecurityContext securityContext,
                                      @HeaderParam("refreshToken") String refreshToken) throws Exception {

        try {
            User user = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            TokenDto tokenDto = authenticationService.getNewAccessToken(user, refreshToken, "ipAdress", "Device");
            return Response.ok(tokenDto).header("ACCESS_TOKEN", tokenDto.getAccessToken())
                    .header("REFRESH_TOKEN", tokenDto.getRefreshToken())
                    .build();
        } catch (TokenNotFoundException e) {
            return Response.status(406, "USER IS LOGGED OUT")
                    .header("status", "USER IS LOGGED OUT").build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER NOT FOUND")
                    .header("status", "USER NOT FOUND").build();
        }
    }

    @RolesAllowed({"USER"})
    @POST
    @Path("/logout/")
    public Response logout(@Context SecurityContext securityContext, @HeaderParam("refreshToken") String refreshToken) {

        try {
            authenticationService.logout(refreshToken);
            return Response.ok().header("STATUS", "USER IS LOGGED OUT").build();
        } catch (TokenNotFoundException e) {
            return Response.status(406, "TOKEN NOT CREATED")
                    .header("status", "TOKEN NOT CREATED").build();
        }
    }
}