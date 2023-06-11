package audit.logs.resource;

import audit.logs.entity.Log;
import audit.logs.exception.DateException;
import audit.logs.exception.PermissionDeniedException;
import audit.logs.service.LogService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;


@Path("/logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LogsResource {

    @Inject
    LogService logService;

    private static final Logger LOGGER = LoggerFactory.getLogger(LogsResource.class);


    @GET
    public Response GetLogs(@HeaderParam("userId") String userId,
                            @HeaderParam("from") String from,
                            @HeaderParam("to") String to,
                            @HeaderParam("action") String action,
                            @HeaderParam("token") String token) {

        try {
            logService.checkPermission(token,"AUDIT_LOGS_GET_ALL");
            List<Log> logs= logService.getLogs(userId,from,to,action);
            return Response.ok(logs).header("STATUS", "LIST OF LOGS")
                    .build();
        } catch (DateException e) {
            return Response.status(406, "DATE ERROR")
                    .header("status", " DATE ERROR ").build();
        } catch (PermissionDeniedException e) {
            return Response.status(406, "PERMISSION DENIED")
                    .header("status", " PERMISSION DENIED ").build();
        }
    }

    @POST
    public Response postLog(@HeaderParam ("action") String action, @HeaderParam("token") String token) {


            try {
                logService.checkPermission(token,"AUDIT_POST_GET_ALL");
                Log savedLog = logService.saveLog(action, token);
                return Response.ok(savedLog).header("STATUS", "LOG IS SAVED")
                        .build();
            }catch (PermissionDeniedException e) {
                return Response.status(406, "PERMISSION DENIED")
                        .header("status", " PERMISSION DENIED ").build();
            }
    }

    @DELETE
    public Response DeleteLogs(@HeaderParam("userId") String userId,
                           @HeaderParam("from") String from,
                           @HeaderParam("to") String to,
                           @HeaderParam("action") String action,
                           @HeaderParam("token") String token) {
        try {
            logService.checkPermission(token,"AUDIT_LOGS_DELETE_ALL");
            logService.deleteLogs(userId,from,to,action);
            return Response.ok().header("STATUS", "LIST OF LOGS IS DELETED")
                    .build();
        } catch (DateException e) {
            return Response.status(406, "DATE ERROR")
                    .header("status", "DATE ERROR ").build();
        }catch (PermissionDeniedException e) {
            return Response.status(406, "PERMISSION DENIED")
                    .header("status", " PERMISSION DENIED ").build();
        }
    }


}