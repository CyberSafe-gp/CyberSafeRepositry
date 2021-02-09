package com.example.cybersafe.Objects;

public class Report {

    private String report_id;
    private String parent_id;
    private String comment_id;
    private String status;


    public Report() {
    }

    public Report(String report_id, String parent_id, String comment_id, String status){
        this.report_id = report_id;
        this.parent_id = parent_id;
        this.comment_id = comment_id;
        this.status = status;
    }

    public String getReport_id() {
        return report_id;
    }

    public void setReport_id(String report_id) {
        this.report_id = report_id;
    }



    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
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

}
