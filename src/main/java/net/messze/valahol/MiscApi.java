package net.messze.valahol;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import net.messze.valahol.data.Comment;
import net.messze.valahol.data.Puzzle;
import net.messze.valahol.data.User;
import net.messze.valahol.service.MongodbPersistence;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

@Path("/misc")
@Api(value = "/puzzle", description = "Other unique operations.")
@Produces({"application/json"})
public class MiscApi {

    @Inject
    MongodbPersistence persistence;

    Map<String, String> userIdToId = new HashMap<String, String>();

    Map<String, String> puzzleIdToId = new HashMap<String, String>();

    @GET
    @ApiOperation(value = "migrate values from the old mysql db")
    public void migrate() throws Exception {
        persistence.getDb().getCollection("user").drop();
        persistence.getDb().getCollection("puzzle").drop();

        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/test?user=monty&password=greatsqldb");
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM USERS");
        while (rs.next()) {
            User u = new User();
            u.setUserName(rs.getString("userName"));
            u.setImage(rs.getString("image"));
            u.setLastLoginDate(rs.getDate("lastLoginDate"));
            u.setUserId(rs.getString("userId"));
            String id = persistence.create(u);
            userIdToId.put(u.getUserId(), id);
        }


        rs = stmt.executeQuery("SELECT * FROM PUZZLES");
        while (rs.next()) {
            Puzzle p = new Puzzle();
            p.setUserId(userIdToId.get(rs.getString("userId")));
            p.setAnswer(rs.getString("answer"));
            p.setDate(rs.getDate("date"));
            p.setLabel(rs.getString("label"));
            p.setLat(rs.getLong("lat"));
            p.setLng(rs.getLong("lng"));
            p.setPitch(rs.getLong("pitch"));
            p.setHeading(rs.getLong("heading"));
            p.setQuestion(rs.getString("question"));
            String id = persistence.create(p);
            puzzleIdToId.put(rs.getString("id"), id);
        }


        rs = stmt.executeQuery("SELECT * FROM COMMENTS");
        while (rs.next()) {
            Comment c = new Comment();
            c.setUserId(userIdToId.get(rs.getString("userId")));
            c.setDate(rs.getDate("date"));
            c.setContent(rs.getString("content"));
            c.setPuzzleId(puzzleIdToId.get(rs.getString("puzzleId")));
            c.setSpoiler(rs.getBoolean("spoiler"));
        }

    }


}
