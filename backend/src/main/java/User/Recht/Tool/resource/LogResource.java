package User.Recht.Tool.resource;


import User.Recht.Tool.dtos.auditLogDto.LogDto;
import User.Recht.Tool.entity.User;
import User.Recht.Tool.exception.Permission.UserNotAuthorized;
import User.Recht.Tool.exception.logs.DateException;
import User.Recht.Tool.service.AuthorizationService;
import User.Recht.Tool.service.LogsService;
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
import java.util.List;

@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogResource {

    @Inject
    LogsService logsService;
    @Inject
    AuthorizationService authorizationService;
    private static final Logger LOGGER = LoggerFactory.getLogger(LogResource.class);


    @GET
    @RolesAllowed({"USER"})
    public Response getLogs (@QueryParam("userId") String userId,
                             @QueryParam("from") String from,
                             @QueryParam("to") String to,
                             @QueryParam("action") String action ,
                             @Context RoutingContext routingContext,
                             @Context SecurityContext securityContext) {

        try {
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            authorizationService.checkExistedUserPermission("AUDIT_LOGS_GET", token);

            List<LogDto> logs = logsService.getLogs(userId, from, to, action, token);

            return Response.ok(logs).header("STATUS", "LISTE OF LOGS")
                    .build();

        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DateException e) {
            return Response.status(406, "THE DATE VALUE SHOULD BI IN THIS FORM dd-MM-yyyy")
                    .header("status", "THE DATE VALUE SHOULD BI IN THIS FORM dd-MM-yyyy ").build();
        }

    }

    @DELETE
    @RolesAllowed({"USER"})
    public Response deleteLogs (@QueryParam("userId") String userId,
                             @QueryParam("from") String from,
                             @QueryParam("to") String to,
                             @QueryParam("action") String action ,
                             @Context RoutingContext routingContext,
                             @Context SecurityContext securityContext) {

        try {
            String token = routingContext.request().getHeader("Authorization").substring(7);

            // CHECK PERMISSIONS
            authorizationService.checkExistedUserPermission("AUDIT_LOGS_DELETE", token);

            logsService.deleteLogs(userId, from, to, action, token);

            return Response.ok().header("STATUS", "LOGS ARE DELETED")
                    .build();

        } catch (UserNotAuthorized e) {
            return Response.status(406, "USER IS NOT AUTHOROZIED FOR THE PERMISSION")
                    .header("STATUS", "USER IS NOT AUTHOROZIED FOR THE PERMISSION").build();
        } catch (DateException e) {
            return Response.status(406, "THE DATE VALUE SHOULD BI IN THIS FORM dd-MM-yyyy")
                    .header("status", "THE DATE VALUE SHOULD BI IN THIS FORM dd-MM-yyyy ").build();
        }

    }


}