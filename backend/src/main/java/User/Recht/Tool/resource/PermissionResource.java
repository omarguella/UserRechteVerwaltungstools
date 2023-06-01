package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.PermissionDtos.PermissionDto;
import User.Recht.Tool.entity.Permission;
import User.Recht.Tool.exception.Permission.PermissionNotFound;
import User.Recht.Tool.exception.Permission.PermissionToRoleNotFound;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.service.PermissionService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/permissions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PermissionResource {

    @Inject
    PermissionService permissionService;

    @POST
    @PermitAll
    public Response addPermissions(@RequestBody PermissionDto permissionDto, @Context SecurityContext securityContext) {

        try {
            List <Permission> permissions = permissionService.createPermission(permissionDto);
            return Response.ok(permissions).header("status", "NEW PERMISSIONS ARE ADDED")
                    .build();

        }  catch (IllegalAccessException e) {

            return Response.status(406, "THE ACTION SHOULD APPEAR ONCE TIME ONE OF THE FOLLOWING ELEMENTS: GET, POST, PUT, DELETE")
                    .header("status", "THE ACTION SHOULD APPEAR ONCE TIME AND ONE OF THE FOLLOWING ELEMENTS: GET, POST, PUT, DELETE")
                    .build();

        }catch (IllegalArgumentException  e) {

            return Response.status(406, "PERMISSION NAME SHOULD BE ONLY CHARACTER")
                    .header("status", "PERMISSION NAME SHOULD BE ONLY CHARACTER")
                    .build();

        } catch (PermissionNotFound e) {
            return Response.status(406, "xxx")
                    .header("status", "xxx")
                    .build();

        }
    }

    @GET
    @PermitAll
    public Response getAllPermissions(@Context SecurityContext securityContext) {

        List<Permission> permissions = permissionService.getAllPermissions();

        return Response.ok(permissions).header("STATUS", "LIST OF PERMISSIONS")
                .build();
    }
    @GET
    @PermitAll
    @Path("name/{name}")
    public Response getPermissionsByName(@PathParam("name") String name,@Context SecurityContext securityContext) {

        List<Permission> permissions = permissionService.getPermissionsByName(name);

        return Response.ok(permissions).header("STATUS", "LIST OF PERMISSIONS")
                .build();
    }

    @GET
    @PermitAll
    @Path("key/{key}")
    public Response getPermissionByKey(@PathParam("key") String key,@Context SecurityContext securityContext) {


        try {
            Permission permission = permissionService.getPermissionByKey(key);

            return Response.ok(permission).header("PERMISSION_KEY", key)
                    .build();
        } catch (PermissionNotFound e) {

            return Response.status(406, "PERMISSION DONT EXIST")
                    .header("status", "PERMISSION DONT EXIST").build();
        }
    }

    @DELETE
    @PermitAll
    @Path("key/{key}")
    public Response deletePermission(@PathParam("key") String key, @Context SecurityContext securityContext) {

        try {
            Permission permissionToDelete = permissionService.deletePermissionByKey(key);

            return Response.ok(permissionToDelete).header("STATUS", "THE PERMISSION_KEY " + key + " IS DELETED")
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        } catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NOT FOUND")
                    .header("status", "PERMISSION NOT FOUND").build();
        } catch (PermissionToRoleNotFound e) {
            return Response.status(406, "PERMISSION TO ROLE NOT SAVE")
                    .header("status", "PERMISSION TO ROLE NOT SAVE").build();
        } catch (IllegalArgumentException e) {
            return Response.status(406, "TYPE SHOULD BE ALL OR ONE")
                    .header("status", "TYPE SHOULD BE ALL OR ONE").build();
        }
    }
    @DELETE
    @PermitAll
    @Path("name/{name}")
    public Response deletePermissionsByName(@PathParam("name") String name, @Context SecurityContext securityContext) {
        try {
            List<Permission> permissionToDelete = permissionService.deletePermissionsByName(name);

            return Response.ok(permissionToDelete).header("STATUS", "THE PERMISSIONS WITH THE NAME  " + name + " are DELETED")
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        } catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NAME NOT FOUND")
                    .header("status", "PERMISSION NAME NOT FOUND").build();
        } catch (PermissionToRoleNotFound e) {
            return Response.status(406, "PERMISSION TO ROLE NOT SAVE")
                    .header("status", "PERMISSION TO ROLE NOT SAVE").build();
        } catch (IllegalArgumentException e) {
            return Response.status(406, "TYPE SHOULD BE ALL OR ONE")
                    .header("status", "TYPE SHOULD BE ALL OR ONE").build();
        }
    }
}