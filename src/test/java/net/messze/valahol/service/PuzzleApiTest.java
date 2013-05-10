package net.messze.valahol.service;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import net.messze.valahol.PuzzleApi;
import net.messze.valahol.data.Highscore;
import net.messze.valahol.data.Puzzle;
import net.messze.valahol.data.Solution;
import net.messze.valahol.data.SolutionResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import java.util.List;

public class PuzzleApiTest {

    private static DB db;

    @BeforeClass
    public static void init() throws Exception {
        db = new MongoClient("localhost", 27017).getDB("valahol");
    }

    @Before
    public void cleanUp() throws Exception {
        TestUtil.defaultState(db);
    }


    @Test
    public void solve() {

        Solution s = new Solution();

        s.setAnswer("a");
        s.setScore(20);
        s.setUserId("518ca1354a047ca1f9fac1cc");

        Response resp = TestUtil.createApi(PuzzleApi.class).solve("518ca0054a0431034b096ca9", s);
        Assert.assertTrue(((SolutionResponse) resp.getEntity()).isGood());
        Assert.assertEquals(4, db.getCollection("solution").count());
        Assert.assertEquals(1, db.getCollection("guess").count());
    }

    @Test
    public void solveWrong() {

        Solution s = new Solution();

        s.setAnswer("ax");
        s.setScore(20);
        s.setUserId("518ca1354a047ca1f9fac1cc");

        Response resp = TestUtil.createApi(PuzzleApi.class).solve("518ca0054a0431034b096ca9", s);
        Assert.assertFalse(((SolutionResponse) resp.getEntity()).isGood());
        Assert.assertEquals(3, db.getCollection("solution").count());
        Assert.assertEquals(2, db.getCollection("guess").count());
    }

    @Test
    public void highscore() {

        PuzzleApi api = TestUtil.createApi(PuzzleApi.class);
        List<Highscore> scores = api.highscores();
        Assert.assertEquals(2, scores.size());
        Highscore first = scores.get(0);
        Assert.assertEquals(60, first.getScore());
        Assert.assertEquals("Kisvig Miska", first.getUserName());
    }


    @Test
    public void findAll() {

        HttpServletRequest request = TestUtil.requestWithUserSession("518ca1354a047ca1f9fac1cc");

        PuzzleApi api = TestUtil.createApi(PuzzleApi.class);

        Response resp = api.list(false, request);
        Assert.assertEquals(2, ((List<Puzzle>) resp.getEntity()).size());

        resp = api.list(true, request);
        Assert.assertEquals(1, ((List<Puzzle>) resp.getEntity()).size());

    }
}
