package User.Recht.Tool.resource;

import User.Recht.Tool.dtos.permissionDtos.ListPermissionKeysDto;
import User.Recht.Tool.dtos.permissionDtos.PermissionRoleDto;
import User.Recht.Tool.exception.Permission.PermissionNotFound;
import User.Recht.Tool.exception.Permission.PermissionToRoleNotFound;
import User.Recht.Tool.exception.role.RoleNotFoundException;
import User.Recht.Tool.exception.superadmin.CannotModifySuperAdminException;
import User.Recht.Tool.service.PermissionToRoleService;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.List;

@Path("/permissionRole")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PermissionToRoleResource {

    @Inject
    PermissionToRoleService permissionToRoleService;

    @POST
    @PermitAll
    public Response addPermissionToUser(@RequestBody PermissionRoleDto permissionRoleDto, @Context SecurityContext securityContext) {
        try {
            PermissionRoleDto permissionRole = permissionToRoleService.addPermissionToRole(permissionRoleDto);
            return Response.ok(permissionRole).header("status", "THE  PERMISSION " + permissionRole.getPermissionKey() + " IS ADDED TO THE ROLE " + permissionRole.getRoleName())
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

    @POST
    @PermitAll
    @Path("/list/")
    public Response addPermissionsListToRole(@RequestBody ListPermissionKeysDto listPermissionKeysDto, @HeaderParam("roleName") String roleName
            , @Context SecurityContext securityContext) {
        try {
            List<String> addedPermissions = permissionToRoleService.addPermissionsListToRole(listPermissionKeysDto, roleName);
            return Response.ok(addedPermissions).header("status", "THE  LIST OF PERMISSIONS  IS ADDED TO THE ROLE " + roleName.toUpperCase())
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
        catch (ArrayIndexOutOfBoundsException e) {
            return Response.status(406, "TYPE PERMISSION_KEY SHOULD BE IN THIS FORM KEY_GET_TYPE")
                    .header("status", "TYPE SHOULD BE ALL OR ONE").build();
        }
    }

    @GET
    @PermitAll
    public Response getPermissionRole(@HeaderParam("permissionKey") String permissionKey, @HeaderParam("roleName") String roleName
            , @Context SecurityContext securityContext) {
        try {
            PermissionRoleDto permissionRole = permissionToRoleService.getPermissionByRole(permissionKey, roleName);
            return Response.ok(permissionRole).header("status", "THE PERMISSION " + permissionKey + " OF THE ROLE " + roleName)
                    .build();
        } catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NOT FOUND")
                    .header("status", "PERMISSION NOT FOUND").build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (PermissionToRoleNotFound e) {
            return Response.status(406, "PERMISSION OF THE ROLE NOT FOUND")
                    .header("status", "PERMISSION OF THE ROLE NOT FOUND").build();
        }
    }

    @GET
    @PermitAll
    @Path("/role/{roleName}")
    public Response getAllPermissionsOfRole(@PathParam("roleName") String roleName
            , @Context SecurityContext securityContext) {
        try {
            List<String> permissionKeys = permissionToRoleService.getAllPermissionsOfRole(roleName);
            return Response.ok(permissionKeys).header("status", "THE LIST OF THE ALL PERMISSIONS OF THE ROLE " + roleName)
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        }
    }

    @PUT
    @PermitAll
    public Response updatePermissionRole(@RequestBody PermissionRoleDto permissionRoleDto, @Context SecurityContext securityContext) {
        try {
            PermissionRoleDto permissionRole = permissionToRoleService.updatePermissionRole(permissionRoleDto);
            return Response.ok(permissionRole).header("status", "THE  PERMISSION " + permissionRole.getPermissionKey() + " OF THE ROLE " + permissionRole.getRoleName() + " IS UPDATED")
                    .build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
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
    public Response deletePermissionRole(@HeaderParam("permissionKey") String permissionKey, @HeaderParam("roleName") String roleName
            , @Context SecurityContext securityContext) {
        try {
            PermissionRoleDto permissionRoleDto = permissionToRoleService.deletePermissionRole(permissionKey, roleName);
            return Response.ok(permissionRoleDto).header("status", "THE PERMISSION " + permissionKey + " OF THE ROLE " + roleName + " IS DELETED")
                    .build();
        } catch (PermissionNotFound e) {
            return Response.status(406, "PERMISSION NOT FOUND")
                    .header("status", "PERMISSION NOT FOUND").build();
        } catch (RoleNotFoundException e) {
            return Response.status(406, "ROLE NOT FOUND")
                    .header("status", "ROLE NOT FOUND").build();
        } catch (PermissionToRoleNotFound e) {
            return Response.status(406, "PERMISSION OF THE ROLE NOT FOUND")
                    .header("status", "PERMISSION OF THE ROLE NOT FOUND").build();
        } catch (CannotModifySuperAdminException e) {
            return Response.status(406, "CANNOT MODIFY A SUPERADMIN")
                    .header("status", "CANNOT MODIFY A SUPERADMIN").build();
        }
    }

    @DELETE
    @PermitAll
    @Path("/list/")
    public Response deleteListePermissionsOfRole(@RequestBody ListPermissionKeysDto listPermissionKeysDto, @HeaderParam("roleName") String roleName
            , @Context SecurityContext securityContext) {
        try {
            List<String> toDeletePermissions = permissionToRoleService.deleteListePermissionsOfRole(listPermissionKeysDto, roleName);
            return Response.ok(toDeletePermissions).header("status", "THE LIST OF PERMISSIONS OF THE ROLE IS DELETED" + roleName.toUpperCase())
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
    @Path("/all/")
    public Response deleteALLPermissionsOfRole(@HeaderParam("roleName") String roleName
            , @Context SecurityContext securityContext) {
        try {
            List<String> toDeletePermissions = permissionToRoleService.deleteALLPermissionsOfRole(roleName);
            return Response.ok(toDeletePermissions).header("status", "ALL PERMISSIONS OF THE ROLE "+ roleName.toUpperCase()+" ARE DELETED")
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

}