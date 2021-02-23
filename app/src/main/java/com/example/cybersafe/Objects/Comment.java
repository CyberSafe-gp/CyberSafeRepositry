package com.example.cybersafe.Objects;

public class Comment {

    private String comment_id;
    private String SMAccountCredentials_id;
    private String sender;
    private String body;
    private Boolean flag;
    private String timestamp;
    private String timeRetrieved;



    public Comment() {
    }

    public Comment(String comment_id, String SMAccountCredentials_id, String sender, String body, Boolean flag, String timestamp, String timeRetrieved) {
        this.comment_id = comment_id;
        this.SMAccountCredentials_id = SMAccountCredentials_id;
        this.sender = sender;
        this.body = body;
        this.flag = flag;
        this.timestamp = timestamp;
        this.timeRetrieved = timeRetrieved;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getSMAccountCredentials_id() {
        return SMAccountCredentials_id;
    }

    public void setSMAccountCredentials_id(String SMAccountCredentials_id) {
        this.SMAccountCredentials_id = SMAccountCredentials_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }



    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }



    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }



    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


    public String getTimeRetrieved() {
        return timeRetrieved;
    }

    public void setTimeRetrieved(String timeRetrieved) {
        this.timeRetrieved = timeRetrieved;
    }


}
