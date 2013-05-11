    package net.messze.valahol;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import net.messze.valahol.data.Comment;
import net.messze.valahol.data.Puzzle;
import net.messze.valahol.data.User;
import net.messze.valahol.service.MongodbPersistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Path("/misc")
@Api(value = "/misc", description = "Other unique operations.")
@Produces({"application/json"})
public class MiscApi {
    private static final Logger LOG = LoggerFactory.getLogger(MiscApi.class);
    @Inject
    MongodbPersistence persistence;

    Map<String, String> userIdToId = new HashMap<String, String>();

    Map<String, String> puzzleIdToId = new HashMap<String, String>();

    @Inject
    @Named("mysqlConnection")
    private String mysqlConnection;

    @GET
    @ApiOperation(value = "migrate values from the old mysql db")
    public String migrate() throws Exception {
        persistence.getDb().getCollection("user").drop();
        persistence.getDb().getCollection("comment").drop();
        persistence.getDb().getCollection("puzzle").drop();

        Connection conn = DriverManager.getConnection(mysqlConnection);
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM users");
        while (rs.next()) {
            User u = new User();
            u.setUserName(rs.getString("userName"));
            u.setImage(rs.getString("image"));

            u.setLastLoginDate(new Date(rs.getLong("lastLoginDate")));
            u.setUserId(rs.getString("userId"));
            String id = persistence.create(u);
            userIdToId.put(u.getUserId(), id);
        }


        rs = stmt.executeQuery("SELECT * FROM puzzles");
        while (rs.next()) {
            Puzzle p = new Puzzle();
            p.setUserId(userIdToId.get(rs.getString("userId")));
            p.setAnswer(rs.getString("answer"));
            p.setDate(new Date(rs.getLong("date")));
            p.setLabel(rs.getString("label"));
            p.setLat(rs.getDouble("lat"));
            p.setLng(rs.getDouble("lng"));
            p.setPitch(rs.getDouble("pitch"));
            p.setHeading(rs.getDouble("heading"));
            p.setQuestion(rs.getString("question"));
            String id = persistence.create(p);
            puzzleIdToId.put(rs.getString("id"), id);
        }


        rs = stmt.executeQuery("SELECT * FROM comments");
        while (rs.next()) {
            Comment c = new Comment();
            c.setUserId(userIdToId.get(rs.getString("userId")));
            c.setDate(new Date(rs.getLong("date")));
            c.setContent(rs.getString("content"));
            c.setPuzzleId(puzzleIdToId.get(rs.getString("puzzleId")));
            c.setSpoiler(rs.getInt("isSpoiler")==1);
        }
        return "{\"ok\":1}";
    }

    public static void main(String args[]) throws Exception {
        Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                GuiceConfig.loadConfiguration(binder());
                bind(MongodbPersistence.class);
                bind(MiscApi.class);
            }
        }).getInstance(MiscApi.class).migrate();


    }


}
