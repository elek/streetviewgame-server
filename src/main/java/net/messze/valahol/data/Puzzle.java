package net.messze.valahol.data;


import org.mongojack.ObjectId;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@XmlRootElement
public class Puzzle {

    @Id
    @ObjectId
    private String id;

    private String label;

    private String question;

    private String answer;

    private double lat;

    private double lng;

    private double heading;

    private double pitch;

    private String userId;

    private Date date = new Date();

    private List<Solver> solvers = new ArrayList();

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getHeading() {
        return heading;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public List<Solver> getSolvers() {
        return solvers;
    }

    public void setSolvers(List<Solver> solvers) {
        this.solvers = solvers;
    }

    public void addSolver(Solver solver) {
        this.solvers.add(solver);
    }
}
