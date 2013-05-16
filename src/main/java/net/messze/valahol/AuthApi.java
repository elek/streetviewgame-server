
package net.messze.valahol;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Tokeninfo;
import com.google.api.services.plus.Plus;
import com.google.api.services.plus.model.Person;
import com.wordnik.swagger.annotations.*;
import net.messze.valahol.data.User;
import net.messze.valahol.service.MongodbPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Date;


@Path("/auth")
@Api(value = "/auth", description = "Authentication operation with different providers")
@Produces("application/json")
@Consumes("application/json")
public class AuthApi {

    private static final JsonFactory JSON_FACTORY = new JacksonFactory();

    private static final Logger LOG = LoggerFactory.getLogger(AuthApi.class);

    public static final String USER_ID = "userId";

    @Inject
    @Named("googleApiSecret")
    String apiSecret;

    @Inject
    @Named("googleApiClientId")
    String clientId;

    @Inject
    MongodbPersistence persistenceService;

    @GET
    @Path("/current")
    @ApiOperation(value = "Get the current user", responseClass = "net.messze.valahol.data.User")
    public User current(@Context javax.servlet.http.HttpServletRequest request) {
        User user = persistenceService.find(User.class, (String) request.getSession().getAttribute(USER_ID));
        return user;
    }

    @POST
    @Path("/google")
    @Consumes("application/x-www-form-urlencoded")
    @ApiOperation(value = "Authenticate with a google plus provider")
    public Response update(String content, @Context HttpServletRequest request) throws GeneralSecurityException, IOException {
        LOG.debug("Google is authenticated with token " + content + " (" + clientId + ":" + apiSecret + ")");

        NetHttpTransport TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(TRANSPORT, JSON_FACTORY, clientId, apiSecret, content, "postmessage").execute();
        // Create a credential representation of the token data.
        GoogleCredential credential = new GoogleCredential.Builder()
                .setJsonFactory(JSON_FACTORY)
                .setTransport(TRANSPORT)
                .setClientSecrets(apiSecret, clientId).build()
                .setFromTokenResponse(tokenResponse);

        // Check that the token is valid.
        Oauth2 oauth2 = new Oauth2.Builder(TRANSPORT, JSON_FACTORY, credential).build();
        Tokeninfo tokenInfo = oauth2.tokeninfo().setAccessToken(credential.getAccessToken()).execute();
        // If there was an error in the token info, abort.
        if (tokenInfo.containsKey("error")) {
            return Response.status(401).build();
            //return GSON.toJson(tokenInfo.get("error").toString());
        }

        // Make sure the token we got is for our app.
        if (!tokenInfo.getIssuedTo().equals(clientId)) {
            return Response.status(401).build();
            /*response.status(401);
            return GSON.toJson("Token's client ID does not match app's.");*/
        }
        // Store the token in the session for later use.
        //request.session().attribute("token", tokenResponse.toString());
        //return GSON.toJson("Successfully connected user.");
        Plus plus = new Plus.Builder(TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName("Google-PlusSample/1.0")
                .build();
        Person me = plus.people().get("me").execute();

        User u = persistenceService.findByUserId(me.getId());
        if (u == null) {
            u = new User();
            u.setUserId(me.getId());
            u.setUserName(me.getDisplayName());
            u.setImage(me.getImage().getUrl());
            u.setLastLoginDate(new Date());
            persistenceService.create(u);
        } else {
            u.setLastLoginDate(new Date());
            persistenceService.update(User.class, u.getId(), u);
        }
        request.getSession().setAttribute(USER_ID, u.getId());
        return Response.ok().entity(u).build();

    }




}
