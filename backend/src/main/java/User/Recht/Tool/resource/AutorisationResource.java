package User.Recht.Tool.resource;

import User.Recht.Tool.service.serviceImpl.AutorisationServiceImpl;
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
public class AutorisationResource {

    @Inject
    AutorisationServiceImpl autorisationService;

    private static final Logger LOGGER = LoggerFactory.getLogger(AutorisationResource.class);

    @GET
    @RolesAllowed({ "USER" })
    public Response getMyPermissions(@Context RoutingContext routingContext, @Context SecurityContext securityContext) {

        String token = routingContext.request().getHeader("Authorization").substring(7);
        return Response.ok(autorisationService.getMyPermissions(token)).build();

    }


    }
