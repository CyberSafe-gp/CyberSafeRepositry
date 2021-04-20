package com.example.cybersafe.Objects;

public class Comment {

    private String comment_id;
    private String SMAccountCredentials_id;
    private String sender;
    private String body;
    private String C_ID;
    private Boolean flag;
    private String Notification;




    public Comment() {
    }

    public Comment(String comment_id, String SMAccountCredentials_id, String sender, String body, String c_ID, Boolean flag) {
        this.comment_id = comment_id;
        this.SMAccountCredentials_id = SMAccountCredentials_id;
        this.sender = sender;
        this.body = body;
        C_ID = c_ID;
        this.flag = flag;
        Notification="new";
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

    public String getC_ID() {
        return C_ID;
    }

    public void setC_ID(String c_ID) {
        C_ID = c_ID;
    }

    public String getNotification() {
        return Notification;
    }

    public void setNotification(String notification) {
        Notification = notification;
    }
}
