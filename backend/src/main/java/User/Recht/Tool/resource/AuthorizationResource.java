package User.Recht.Tool.resource;

import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.PermissionNotFound;
import User.Recht.Tool.exception.user.UserNotFoundException;
import User.Recht.Tool.service.UserService;
import User.Recht.Tool.service.serviceImpl.AuthorizationServiceImpl;
import io.vertx.ext.web.RoutingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/auto")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthorizationResource {

    @Inject
    AuthorizationServiceImpl autorisationService;

    @Inject
    UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthorizationResource.class);

    @GET
    @RolesAllowed({"USER"})
    @Path("/allPermissions")
    public Response getMyPermissions(@Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        String token = routingContext.request().getHeader("Authorization").substring(7);
        return Response.ok(autorisationService.getMyPermissions(token)).build();
    }

    @GET
    @RolesAllowed({"USER"})
    @Path("/apiAccess")
    public Response verifyingAPIAccessAuthorization(@HeaderParam("PermissionKey") String permissionKey, @Context RoutingContext routingContext, @Context SecurityContext securityContext) throws UserNotFoundException {

        try {
            User connectedUser = userService.getUserByEmail(securityContext.getUserPrincipal().getName());
            return Response.ok(autorisationService.verifyingAPIAccessAuthorization(connectedUser, permissionKey))
                    .header("status", "YOU HAVE ACCESS TO THE API").build();

        } catch (IllegalArgumentException e) {
            return Response.status(406, "TYPE SHOULD BE ALL OR ME")
                    .header("status", "TYPE SHOULD BE ALL OR ME").build();
        }catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NOT FOUND")
                    .header("status", "PERMISSION NOT FOUND").build();
        }
    }


}
