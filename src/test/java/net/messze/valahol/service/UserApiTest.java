package net.messze.valahol.service;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import net.messze.valahol.UserApi;
import net.messze.valahol.data.Solution;
import net.messze.valahol.data.User;
import net.messze.valahol.data.UserDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class UserApiTest {
    private static DB db;

    @BeforeClass
    public static void init() throws Exception {
        MongoClient mongoClient = new MongoClient("localhost", 27017);
        db = mongoClient.getDB("valahol");
    }

    @Before
    public void cleanUp() throws Exception {
        TestUtil.defaultState(db);
    }

    @Test
    public void get() {
        UserApi api = TestUtil.createApi(UserApi.class);
        UserDetails user = (UserDetails) api.get("518ca1354a047ca1f9fac1cc").getEntity();
        Assert.assertEquals("Hutira Geza", user.getUser().getUserName());
        Assert.assertEquals(1, user.getCreatedPuzzles().size());
        for (Solution sol : user.getSolvedPuzzles()) {
            System.out.println(sol.getScore());
            System.out.println(sol.getUserId());
            System.out.println(sol.getPuzzleId());
        }
        Assert.assertEquals(2, user.getSolvedPuzzles().size());
    }

    @Test
    public void all() {
        UserApi api = TestUtil.createApi(UserApi.class);

        List<User> user = (List<User>) api.all().getEntity();
        Assert.assertEquals(2,user.size());

    }


}
