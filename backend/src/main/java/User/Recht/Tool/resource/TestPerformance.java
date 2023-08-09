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

@Path("/loaderio-1a08b6c76a1dbdc92a0b983cd500fb2d/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TestPerformance {

    @Inject
    UserService userService;


    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public File getVerificationTokenFile() {
        return new File("uploads/loaderio-1a08b6c76a1dbdc92a0b983cd500fb2d.txt");
    }
    @GET
    @PermitAll
    @Path("/performance/")
    public Response createUser() {

        List<User> users = userService.performanceTest();
        return Response.ok(users).build();
    }
}