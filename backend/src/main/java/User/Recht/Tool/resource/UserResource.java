package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.UserDto;
import User.Recht.Tool.exception.DuplicateElementException;
import User.Recht.Tool.exception.UserNotFoundException;
import User.Recht.Tool.exception.userNameDuplicateElementException;
import User.Recht.Tool.service.UserService;
import User.Recht.Tool.entity.User;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
    @Inject
    UserService userService;

    @POST
    @PermitAll
    @Path("registration/role/{type}")
    public Response createUser(@RequestBody UserDto userDto, @PathParam("type") String type)
            throws UserNotFoundException, Exception {
        try {
            User user = userService.createUser(userDto);

                return Response.ok(userService.getUserByEmail(userDto.getEmail())).header("Email", userDto.getEmail())
                        .build();


        } catch (userNameDuplicateElementException e) {
            return Response.status(406, "Username is already used ")
                    .header("status", " Username is already used ").build();
        } catch (DuplicateElementException e) {
            return Response.status(406, "Email is already used ")
                    .header("status", " Email is already used ").build();
        } catch (NullPointerException a) {
            return Response.status(406, "email , Password or Username not found ")
                    .header("status", " Email , Password and Username are Required ").build();

        }
    }

}
