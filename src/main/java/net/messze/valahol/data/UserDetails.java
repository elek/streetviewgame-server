package net.messze.valahol.data;

import java.util.ArrayList;
import java.util.List;


public class UserDetails {

    private User user;

    private List<Puzzle> createdPuzzles = new ArrayList<Puzzle>();

    private List<Solution> solvedPuzzles = new ArrayList<Solution>();

    private int sumScore;

    private int noSolvedPuzzles;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Solution> getSolvedPuzzles() {
        return solvedPuzzles;
    }

    public void setSolvedPuzzles(List<Solution> solvedPuzzles) {
        this.solvedPuzzles = solvedPuzzles;
    }

    public void addCreatedPuzzle(Puzzle next) {
        createdPuzzles.add(next);
    }

    public List<Puzzle> getCreatedPuzzles() {
        return createdPuzzles;
    }

    public void setCreatedPuzzles(List<Puzzle> createdPuzzles) {
        this.createdPuzzles = createdPuzzles;
    }

    public int getSumScore() {
        return sumScore;
    }

    public void setSumScore(int sumScore) {
        this.sumScore = sumScore;
    }

    public int getNoSolvedPuzzles() {
        return noSolvedPuzzles;
    }

    public void setNoSolvedPuzzles(int noSolvedPuzzles) {
        this.noSolvedPuzzles = noSolvedPuzzles;
    }

    public void addSolution(Solution next) {
        this.solvedPuzzles.add(next);
    }
}
