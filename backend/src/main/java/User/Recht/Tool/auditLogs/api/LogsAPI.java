package User.Recht.Tool.auditLogs.api;


import User.Recht.Tool.dtos.auditLogDto.LogDto;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;


@RegisterRestClient(configKey = "audit-logs")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/logs")
public interface LogsAPI {

    @GET
    List<LogDto> getLogs (@HeaderParam("userId") String userId,
                         @HeaderParam("from") String from,
                         @HeaderParam("to") String to,
                         @HeaderParam("action") String action,
                         @HeaderParam("token") String token);
    @DELETE
    void deleteLogs (@HeaderParam("userId") String userId,
                         @HeaderParam("from") String from,
                         @HeaderParam("to") String to,
                         @HeaderParam("action") String action,
                         @HeaderParam("token") String token);

    @DELETE
    @Path("/id")
    void deleteLog (@HeaderParam("id") Long id,@HeaderParam("token") String token);

    @POST
    LogDto saveLog( @HeaderParam("action") String action, @HeaderParam("token") String token);

}
