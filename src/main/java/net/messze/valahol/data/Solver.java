package net.messze.valahol.data;

import java.util.Date;

/**
 * A good solution for a puzzle. Element of the hall of fame lists.
 */
public class Solver {

    private String userId;

    private String name;

    private int score;

    private Date date;

    public Solver() {
    }

    public Solver(String userId, String name, int score, Date date) {
        this.userId = userId;
        this.name = name;
        this.score = score;
        this.date = date;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
