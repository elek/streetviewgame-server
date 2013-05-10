package net.messze.valahol.data;

import org.mongojack.ObjectId;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@XmlRootElement
public class Comment {

    @Id
    @ObjectId
    private String id;

    private String userId;

    private String puzzleId;

    private boolean isSpoiler;

    private String content;

    private Date date = new Date();

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

    public String getPuzzleId() {
        return puzzleId;
    }

    public void setPuzzleId(String puzzleId) {
        this.puzzleId = puzzleId;
    }

    public boolean isSpoiler() {
        return isSpoiler;
    }

    public void setSpoiler(boolean spoiler) {
        isSpoiler = spoiler;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
