package net.messze.valahol.data;


import org.mongojack.ObjectId;

import javax.persistence.Id;
import java.util.Date;

public class Guess {

    @Id
    @ObjectId
    private String id;

    private String userId;

    private String answer;

    private String puzzleId;

    private String puzzleName;

    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(String puzzleId) {
        this.puzzleId = puzzleId;
    }

    public String getPuzzleName() {
        return puzzleName;
    }

    public void setPuzzleName(String puzzleName) {
        this.puzzleName = puzzleName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
