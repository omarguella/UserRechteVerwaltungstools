package User.Recht.Tool.resource;


import User.Recht.Tool.dtos.login.AuthenticationDto;
import User.Recht.Tool.dtos.token.TokenDto;
import User.Recht.Tool.exception.Authentification.WrongPasswordException;
import User.Recht.Tool.exception.Token.TokenNotFoundException;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.AuthenticationService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    @Inject
    AuthenticationService authenticationService;

    @PermitAll
    @POST
    @Path("/login")
    public Response login(@RequestBody AuthenticationDto authenticationDto
            , @Context SecurityContext securityContext) throws Exception {

        try{
        TokenDto  tokenDto = authenticationService.login(authenticationDto,"ipadress","devicename");
            return Response.ok(tokenDto).header("ACCESS_TOKEN", tokenDto.getAccessToken())
                    .header("REFRESH_TOKEN", tokenDto.getRefreshToken())
                    .build();
        } catch (TokenNotFoundException e) {
            return Response.status(406, "TOKEN NOT CREATED")
                    .header("status", "TOKEN NOT CREATED").build();
        } catch (WrongPasswordException e) {
            return Response.status(406, "WRONG PASSWORD")
                    .header("status", "WRONG PASSWORD").build();
        }catch (UserNotFoundException e) {
            return Response.status(406, "USER NOT FOUND")
                    .header("status", "USER NOT FOUND").build();
        }
    }

}