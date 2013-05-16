package net.messze.valahol;

import com.wordnik.swagger.annotations.*;
import net.messze.valahol.data.User;
import net.messze.valahol.service.MongodbPersistence;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Developer API, available only in the test environment.
 */
@Path("/dev")
@Api(value = "/dev", description = "Development helper tools, available only in the test environment")
@Produces("application/json")
public class DevApi {

    @Inject
    MongodbPersistence persistenceService;

    @POST
    @Path("/login")
    @ApiOperation(value = "Authenticate in the development environment. Only for test purposes. ", notes = "user google plus user id OR the internal user id to set the ")
    @ApiResponse(value = "Content of the userId session variable (the internal userId)")
    @ApiErrors({
            @ApiError(code = 400, reason = "Bad request. Both the userId and id parameter are empty."),
            @ApiError(code = 404, reason = "If the specified user doesn't exist")
    })
    public Response auth(@ApiParam(value = "Exeternal userId (eg. the google plusId). User should be exists in the db.") @QueryParam("userId") String userId,
                         @ApiParam(value = "Internal user id") @QueryParam("id") String id,
                         @Context HttpServletRequest request) {
        if (id != null) {
            request.getSession().setAttribute(AuthApi.USER_ID, id);
            return Response.ok(id).build();
        } else if (userId != null) {
            User user = persistenceService.findByUserId(userId);
            if (user == null) {
                return Response.status(Response.Status.NOT_FOUND).build();
            } else {
                request.getSession().setAttribute(AuthApi.USER_ID, user.getId());
                return Response.ok(user.getId()).build();
            }
        } else {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

    }
}
