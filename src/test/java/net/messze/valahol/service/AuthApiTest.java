package net.messze.valahol.service;


import com.mongodb.DB;
import com.mongodb.MongoClient;
import net.messze.valahol.AuthApi;
import net.messze.valahol.data.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AuthApiTest {

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
        AuthApi api = TestUtil.createApi(AuthApi.class);

        User user = api.current(TestUtil.requestWithUserSession("518ca1354a047ca1f9fac1cc"));
        Assert.assertNotNull(user);
        Assert.assertEquals("Hutira Geza", user.getUserName());

    }

}
