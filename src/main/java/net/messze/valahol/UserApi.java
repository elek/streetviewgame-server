
package net.messze.valahol;

import com.wordnik.swagger.annotations.*;
import net.messze.valahol.data.UserDetails;
import net.messze.valahol.service.MongodbPersistence;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 * Example resource class hosted at the URI path "/myresource"
 */
@Path("/user")
@Api(value = "/user", description = "Operations about users")
@Produces("application/json")
public class UserApi {

    @Inject
    MongodbPersistence persistenceService;

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Find user by ID", responseClass = "net.messze.valahol.data.UserDetails")
    @ApiErrors(value = {@ApiError(code = 404, reason = "No user for the given id")})
    public Response get(@ApiParam(value = "ID of user that needs to be fetched", required = true)
                        @PathParam("id") String id) {
        UserDetails user = persistenceService.findUser(id, true);
        if (user == null) {
            return Response.status(404).build();
        } else {
            return Response.ok(user).build();
        }
    }

//    @POST
//    @Path("/{id}")
//    @ApiOperation(value = "Update a specific user", notes = "Only the current user could be updated.")
//    @Consumes("application/json")
//    public void update(@ApiParam(value = "ID of user that needs to be updated", required = true)
//                       @PathParam("id") String id,
//                       @ApiParam(value = "User object to update", required = true) User user) {
//
//    }

    /*@POST
    @Consumes("application/json")
    @ApiOperation(value = "Create a new user",)
    public User create(@ApiParam(value = "User object to create", required = true) User user,
                       @Context javax.servlet.http.HttpServletRequest req) {
        req.getSession().setAttribute("user",user.getUserId());
        return user;
    } */

}
