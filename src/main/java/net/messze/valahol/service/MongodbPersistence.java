package net.messze.valahol.service;

import com.google.inject.Inject;
import com.mongodb.*;
import net.messze.valahol.data.*;
import org.bson.types.ObjectId;
import org.mongojack.DBCursor;
import org.mongojack.DBQuery;
import org.mongojack.JacksonDBCollection;
import org.mongojack.WriteResult;

import javax.inject.Named;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MongodbPersistence {

    Map<Class, JacksonDBCollection<?, String>> collections = new HashMap<Class, JacksonDBCollection<?, String>>();

    DB db;



    @Inject
    public MongodbPersistence(@Named("mongoHost") String mongoHost, @Named("mongoPort") String mongoPort, @Named("mongoDb") String mongoDb) {
        MongoClient mongoClient = null;
        try {
            mongoClient = new MongoClient(mongoHost, Integer.parseInt(mongoPort.trim()));
        } catch (UnknownHostException e) {
            throw new AssertionError(e);
        }
        db = mongoClient.getDB(mongoDb);

    }


    public <T> T find(Class<? extends T> type, String id) {
        return (T) getCollection(type).findOneById(id);
    }


    public <T> List<T> findAll(Class<? extends T> type, String userId) {
        List l = new ArrayList<T>();
        DBCursor<T> cursor;
        if (userId != null) {
            cursor = getCollection(type).find(DBQuery.is("userId", userId)).sort(new BasicDBObject("date", -1));
        } else {
            cursor = getCollection(type).find().sort(new BasicDBObject("date", -1));
        }
        while (cursor.hasNext()) {
            l.add(cursor.next());
        }
        return l;

    }

    private <T> JacksonDBCollection<T, String> getCollection(Class<? extends T> type) {
        if (!collections.containsKey(type)) {
            collections.put(type, JacksonDBCollection.wrap(db.getCollection(type.getSimpleName().toLowerCase()), type, String.class));
        }
        return (JacksonDBCollection<T, String>) collections.get(type);
    }


    public <T> void delete(Class<T> type, String id) {
        getCollection(type).removeById(id);
    }


    public <T> void update(Class<T> type, String id, T data) {
        getCollection(type).updateById(id, data);
    }


    public <T> String create(T obj) {
        WriteResult<Object, String> result = getCollection(obj.getClass()).insert(obj);
        return result.getSavedId();
    }


    public List<Comment> findCommentsForPost(String id) {
        JacksonDBCollection<Comment, String> coll = getCollection(Comment.class);
        DBCursor<Comment> cur = coll.find(DBQuery.is("puzzleId", "id"));
        List<Comment> result = new ArrayList<Comment>();
        while (cur.hasNext()) {
            result.add(cur.next());
        }
        return result;
    }


    public UserDetails findUser(String id, boolean extended) {
        JacksonDBCollection<User, String> userColl = getCollection(User.class);
        JacksonDBCollection<Puzzle, String> puzzleColl = getCollection(Puzzle.class);
        UserDetails userDetails = new UserDetails();
        User user = userColl.findOne(DBQuery.is("_id", id));
        if (user == null) {
            return null;
        } else {
            userDetails.setUser(user);
            if (extended) {
                DBCursor<Puzzle> puzzles = puzzleColl.find(DBQuery.is("userId", user.getId()));
                while (puzzles.hasNext()) {
                    userDetails.addCreatedPuzzle(puzzles.next());
                }
                DBCursor<Solution> solutions = getCollection(Solution.class).find(DBQuery.is("userId", user.getId()));
                while (solutions.hasNext()) {
                    userDetails.addSolution(solutions.next());
                }
            }
            return userDetails;
        }
    }

    public List<Highscore> highscore() {
        String query = "[{$project : {'userId':1,'score':1,'type':1}},{$match : {'type':1}},{$group: {'_id':'$userId','score':{$sum : '$score'}}},{$sort:{'score':-1}}]";
        List<Highscore> result = new ArrayList<Highscore>();

        DBObject project = BasicDBObjectBuilder.start().push("$project").add("userId", 1).add("score", 1).pop().get();
        DBObject group = BasicDBObjectBuilder.start().push("$group").add("_id", "$userId").push("score").add("$sum", "$score").pop().pop().get();
        DBObject sort = BasicDBObjectBuilder.start().push("$sort").add("score", -1).pop().get();

        AggregationOutput output = db.getCollection("solution").aggregate(project, group, sort);
        DBCollection userCollection = db.getCollection("user");
        for (DBObject rec : output.results()) {
            Highscore element = new Highscore();
            String userId = rec.get("_id").toString();
            element.setUserId(userId);
            element.setScore((Integer) rec.get("score"));
            DBObject q = new BasicDBObject("_id", new ObjectId(userId));
            DBObject user = userCollection.findOne(q);
            element.setUserName(user.get("userName").toString());
            element.setUserImage(user.get("image").toString());
            result.add(element);
        }
        return result;
    }

    public DB getDb() {
        return db;
    }

    public void setDb(DB db) {
        this.db = db;
    }

    public User findByUserId(String userId) {
        return getCollection(User.class).findOne(DBQuery.is("userId",userId));
    }
}
