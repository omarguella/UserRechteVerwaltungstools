package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.DeviceInfosDto;
import User.Recht.Tool.dtos.login.AuthenticationDto;
import User.Recht.Tool.dtos.tokenDtos.TokenDto;
import User.Recht.Tool.dtos.userDtos.UserDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Authentification.WrongPasswordException;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.Permission.PublicRoleNotFound;
import User.Recht.Tool.exception.Permission.SessionTimeoutException;
import User.Recht.Tool.exception.Token.TokenNotFoundException;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.user.UserNameDuplicateElementException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.AuthenticationService;
import User.Recht.Tool.service.LogsService;
import User.Recht.Tool.service.UserService;;
import io.vertx.ext.web.RoutingContext;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.ValidationException;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    @Inject
    AuthenticationService authenticationService;
    @Inject
    UserService userService;
    @ConfigProperty(name = "quarkus.http.proxy.proxy-address-forwarding", defaultValue = "false")
    boolean proxyAddressForwarding;

    @Inject
    LogsService logsService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationResource.class);


    @POST
    @PermitAll
    @Path("registration/{roleName}")
    public Response createUser(@RequestBody UserDto userDto, @PathParam("roleName") String roleName) {

        try {
            User user = authenticationService.createPublicUser(userDto, roleName);
            return Response.ok(user).header("Email", userDto.getEmail())
                    .build();

        } catch (UserNameDuplicateElementException e) {
            return Response.status(406, "USERNAME IS ALREADY USED").header("status", " USERNAME IS ALREADY USED ").build();
        } catch (DuplicateElementException e) {
            return Response.status(406, "EMAIL IS ALREADY USED").header("status", " EMAIL IS ALREADY USED").build();
        } catch (ValidationException a) {
            return Response.status(406, "EMAIL, PASSWORD OR PHONENUMBER IS NOT VALID").header("status", "EMAIL, PASSWORD OR PHONENUMBER IS NOT VALID").build();
        } catch (RoleNotFoundException a) {
            return Response.status(406, "ROLE NOT FOUND").header("status", "ROLE NOT FOUND").build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER NOT SAVED").header("status", "USER NOT SAVED").build();
        } catch (PublicRoleNotFound e) {
            return Response.status(406, "THE ROLE IS NOT PUBLIC").header("status", "THE ROLE IS NOT PUBLIC").build();
        }
    }


    @PermitAll
    @POST
    @Path("/login")
    public Response login(@Context HttpHeaders headers, RoutingContext routingContext,
                          @RequestBody AuthenticationDto authenticationDto, @Context SecurityContext securityContext) throws Exception {

        try {

            // Device Infos
            DeviceInfosDto deviceInfos = authenticationService.setDeviceInfos(headers, routingContext, proxyAddressForwarding);

            //Token Generate
            TokenDto tokenDto = authenticationService.login(authenticationDto, deviceInfos);

            // Send Logs
            logsService.saveLogs("LOGIN",tokenDto.getAccessToken());

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
    @Path("/me/")
    public Response getMe( @Context RoutingContext routingContext,@Context SecurityContext securityContext) {
        try {

            String token = routingContext.request().getHeader("Authorization").substring(7);
            // Send Logs
            logsService.saveLogs("GET_MY_SELF",token);

            User user = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            return Response.ok(user).header("EMAIL", user.getEmail())
                    .build();
        } catch (UserNotFoundException e) {
            return Response.status(401, "USER NOT FOUND")
                    .header("status", "USER NOT FOUND").build();
        }
    }

    @RolesAllowed({"USER"})
    @GET
    @Path("/getAccessToken/")
    public Response getNewAccessToken(@Context HttpHeaders headers, RoutingContext routingContext,
                                      @Context SecurityContext securityContext,
                                      @HeaderParam("refreshToken") String refreshToken) throws Exception {

        try {
            DeviceInfosDto deviceInfos = authenticationService.setDeviceInfos(headers, routingContext, proxyAddressForwarding);
            User user = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            TokenDto tokenDto = authenticationService.getNewAccessToken(user, refreshToken, deviceInfos);
            return Response.ok(tokenDto).header("ACCESS_TOKEN", tokenDto.getAccessToken())
                    .header("REFRESH_TOKEN", tokenDto.getRefreshToken())
                    .build();
        } catch (TokenNotFoundException e) {
            return Response.status(401, "USER IS LOGGED OUT")
                    .header("status", "USER IS LOGGED OUT").build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER NOT FOUND")
                    .header("status", "USER NOT FOUND").build();
        } catch (SessionTimeoutException e) {
            return Response.status(419, "SESSION TIMEOUT")
                    .header("status", "SESSION TIMEOUT").build();
        }
    }

    @RolesAllowed({"USER"})
    @POST
    @Path("/logout/")
    public Response logout(@Context SecurityContext securityContext,@Context RoutingContext routingContext, @HeaderParam("refreshToken") String refreshToken) {

        try {
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // Send Logs
            logsService.saveLogs("LOGOUT",token);

            authenticationService.logout(refreshToken);
            return Response.ok().header("STATUS", "USER IS LOGGED OUT").build();
        } catch (TokenNotFoundException e) {
            return Response.status(406, "TOKEN NOT CREATED")
                    .header("status", "TOKEN NOT CREATED").build();
        }
    }

    @RolesAllowed({"USER"})
    @POST
    @Path("logoutAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response logoutAll(@Context SecurityContext securityContext,@Context RoutingContext routingContext) {

        try {
            String token = routingContext.request().getHeader("Authorization").substring(7);
            // Send Logs
            logsService.saveLogs("LOGOUT_ALL",token);

            User user = userService.getUserByEmail(securityContext.getUserPrincipal().getName());

            authenticationService.logoutAll(user.getId());
            return Response.ok().header("STATUS", "ALL DEVICES ARE LOGGED OUT").build();
        } catch (TokenNotFoundException e) {
            return Response.status(406, "TOKEN NOT CREATED")
                    .header("status", "TOKEN NOT CREATED").build();
        } catch (UserNotFoundException e) {
            return Response.status(406, "USER NOT FOUND")
                    .header("status", "USER NOT FOUND").build();
        }

    }


}