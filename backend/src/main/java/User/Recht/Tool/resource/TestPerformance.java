package User.Recht.Tool.resource;


import User.Recht.Tool.service.UserService;
import User.Recht.Tool.entity.User;


import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.Path;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.List;

@Path("/loaderio-9d16910805a5fb836347cdb39de4ec04/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestPerformance {

    @Inject
    UserService userService;


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getVerificationTokenFile() {
        return "loaderio-9d16910805a5fb836347cdb39de4ec04";
    }
    @GET
    @PermitAll
    @Path("/performance/")
    public Response createUser() {

        List<User> users = userService.performanceTest();
        return Response.ok(users).build();
    }
}