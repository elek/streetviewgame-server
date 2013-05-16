package net.messze.valahol.service;


import com.mongodb.DB;
import com.mongodb.MongoClient;
import net.messze.valahol.AuthApi;
import net.messze.valahol.DevApi;
import net.messze.valahol.data.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.core.Response;

public class DevTest {

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
    public void auth() {
        DevApi api = TestUtil.createApi(DevApi.class);

        Response response = api.auth(null, "518ca1354a047ca1f9fac1cc", TestUtil.requestWithUserSession(null));
        Assert.assertNotNull(response.getEntity());
        String s = (String) response.getEntity();
        Assert.assertEquals("518ca1354a047ca1f9fac1cc",s);

    }

}
