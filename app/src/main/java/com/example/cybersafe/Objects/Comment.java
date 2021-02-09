package com.example.cybersafe.Objects;

public class Comment {

    private String comment_id;
    private String SMAccountCredentials_email;
    private String sender;
    private String body;
    private String flag;
    private String timestamp;
    private String timeRetrieved;



    public Comment() {
    }

    public Comment(String comment_id, String SMAccountCredentials_email, String sender, String body, String flag, String timestamp, String timeRetrieved) {
        this.comment_id = comment_id;
        this.SMAccountCredentials_email = SMAccountCredentials_email;
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



    public String getSMAccountCredentials_email() {
        return SMAccountCredentials_email;
    }

    public void setSMAccountCredentials_email(String SMAccountCredentials_email) {
        this.SMAccountCredentials_email = SMAccountCredentials_email;
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



    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
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
