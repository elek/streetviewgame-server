package net.messze.valahol;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.jaxrs.listing.ApiListing;

import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 * Service to return with the generated api docs.
 */
@Path("/api-docs")
@Api("/api-docs")
@Produces({"application/json"})
public class SwaggerApiList extends ApiListing {
}
