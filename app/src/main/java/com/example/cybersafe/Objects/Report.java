package com.example.cybersafe.Objects;

public class Report implements Comparable< Report > {

    private String report_id;
    private String sender_id;
    private String receiver_id;
    private String comment_id;
    private String status;


    public Report() {
    }

    public Report(String report_id, String sender_id, String receiver_id, String comment_id, String status) {
        this.report_id = report_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.comment_id = comment_id;
        this.status = status;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(Report o) {
        return this.getStatus().compareTo(o.getStatus());
    }
}
